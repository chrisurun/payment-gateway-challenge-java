package com.checkout.payment.gateway.unit;

import static com.checkout.payment.gateway.exception.CommonExceptionHandler.INVALID_REQUEST;
import static com.checkout.payment.gateway.exception.CommonExceptionHandler.PAYMENT_NOT_FOUND;
import static com.checkout.payment.gateway.util.Validator.CARD_EXPIRED;
import static com.checkout.payment.gateway.util.Validator.INVALID_CVV;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.BaseTest;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest extends BaseTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    var payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(PaymentStatus.AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour("4321");

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value(PAYMENT_NOT_FOUND));
  }

  @Test
  void whenPaymentRequestHasMissingCvvThenCvvErrorIsReturned() throws Exception {
    var payment = createDummyPostPaymentRequest();
    payment.setCvv(null);
    mvc.perform(MockMvcRequestBuilders.post("/payment")
            .content(toJsonBody(payment))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_REQUEST))
        .andExpect(jsonPath("$.errors[0].field").value("cvv"))
        .andExpect(jsonPath("$.errors[0].message").value(INVALID_CVV));
  }

  @Test
  void whenPaymentRequestHasPastExpiryThenDateErrorIsReturned() throws Exception {
    var payment = createDummyPostPaymentRequest();
    payment.setExpiryYear(2020);
    mvc.perform(MockMvcRequestBuilders.post("/payment")
            .content(toJsonBody(payment))
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_REQUEST))
        .andExpect(jsonPath("$.errors[0].field").value("expiryDate"))
        .andExpect(jsonPath("$.errors[0].message").value(CARD_EXPIRED));
  }
}
