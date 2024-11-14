package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class HealthService {

  private static final Logger LOG = LoggerFactory.getLogger(HealthService.class);

  private final PaymentsRepository paymentsRepository;
  private final PaymentGatewayService paymentGatewayService;

  public HealthService(PaymentsRepository paymentsRepository,
      PaymentGatewayService paymentGatewayService) {
    this.paymentsRepository = paymentsRepository;
    this.paymentGatewayService = paymentGatewayService;
  }

  public HealthResponse getHealth() {
    return new HealthResponse(
        getDbHealth(),
        getAcquirerHealth()
    );
  }

  private boolean getDbHealth() {
    try {
      paymentsRepository.get(UUID.randomUUID());
    } catch (Exception e) {
      LOG.error("Error while checking DB health", e);
      return false;
    }
    return true;
  }

  private boolean getAcquirerHealth() {
    try {
      var dummyPayment = new PostPaymentRequest();
      dummyPayment.setAmount(100);
      dummyPayment.setCardNumber("2222405343248877");
      dummyPayment.setCurrency("GBP");
      dummyPayment.setCvv(123);
      dummyPayment.setExpiryMonth(4);
      dummyPayment.setExpiryYear(2025);
      paymentGatewayService.processPayment(dummyPayment);
    } catch (Exception e) {
      LOG.error("Error while checking Acquirer health", e);
      return false;
    }
    return true;
  }


}
