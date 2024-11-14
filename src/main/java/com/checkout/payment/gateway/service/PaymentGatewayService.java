package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.AcquirerResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.checkout.payment.gateway.enums.PaymentStatus.AUTHORIZED;
import static com.checkout.payment.gateway.enums.PaymentStatus.DECLINED;
import static java.lang.String.valueOf;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final AcquirerClient acquirerClient;

  public PaymentGatewayService(PaymentsRepository paymentsRepository,
      AcquirerClient acquirerClient) {
    this.paymentsRepository = paymentsRepository;
    this.acquirerClient = acquirerClient;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    // At this point we should have already validated the request
    var paymentId = UUID.randomUUID();
    try {
      var acquirerResponse = acquirerClient.processPayment(mapToAcquirerRequest(paymentRequest));
      var postPaymentResponse = mapToPostPaymentResponse(paymentId, paymentRequest, acquirerResponse);
      paymentsRepository.add(postPaymentResponse);
      return postPaymentResponse;
    } catch (Exception e) {
      LOG.error("Error while processing payment", e);
      throw new EventProcessingException("Error while processing payment");
    }
  }

  private AcquirerRequest mapToAcquirerRequest(PostPaymentRequest paymentRequest) {
    return new AcquirerRequest(
        valueOf(paymentRequest.getCardNumber()),
        paymentRequest.getExpiryDate(),
        paymentRequest.getCurrency(),
        paymentRequest.getAmount(),
        paymentRequest.getCvv()
    );
  }

  private PostPaymentResponse mapToPostPaymentResponse(
      UUID paymentId,
      PostPaymentRequest paymentRequest,
      AcquirerResponse acquirerResponse) {
    var response = new PostPaymentResponse();
    response.setId(paymentId);
    response.setStatus(acquirerResponse.getAuthorized() ? AUTHORIZED : DECLINED);
    response.setCardNumberLastFour(getLastFour(paymentRequest.getCardNumber()));
    var date = paymentRequest.getExpiryDate().split("/");
    response.setExpiryMonth(Integer.parseInt(date[0]));
    response.setExpiryYear(2000 + Integer.parseInt(date[1]));
    return response;
  }

  private String getLastFour(String cardNumber) {
    return cardNumber.substring(cardNumber.length() - 4);
  }
}
