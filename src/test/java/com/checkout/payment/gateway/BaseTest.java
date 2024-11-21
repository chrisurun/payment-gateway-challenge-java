package com.checkout.payment.gateway;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static java.time.YearMonth.now;

public class BaseTest {

  protected static String AUTHORISED_PAN = "2222405343248877";

  protected PostPaymentRequest createDummyPostPaymentRequest() {
    var payment = new PostPaymentRequest();
    var now  = now();
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setExpiryMonth(now.getMonthValue());
    payment.setExpiryYear(now.getYear());
    payment.setCardNumber("4242424242424242");
    payment.setCvv("123");
    return payment;
  }

  protected PostPaymentRequest createAuthorisedPostPaymentRequest() {
    var payment = new PostPaymentRequest();
    payment.setAmount(100);
    payment.setCurrency("GBP");
    payment.setExpiryMonth(4);
    payment.setExpiryYear(2025);
    payment.setCardNumber(AUTHORISED_PAN);
    payment.setCvv("123");
    return payment;
  }

  protected String toJsonBody(Object obj) throws Exception {
    var mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(obj);
  }
}
