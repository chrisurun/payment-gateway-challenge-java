package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.client.AcquirerClient;
import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class HealthService {

  private static final Logger LOG = LoggerFactory.getLogger(HealthService.class);

  private final PaymentsRepository paymentsRepository;
  private final AcquirerClient acquirerClient;

  public static final String UNAUTH_PAN = "2222405343248112";
  public static final String UNAUTH_DATE = "01/2026";
  public static final String UNAUTH_CURRENCY = "USD";
  public static final int UNAUTH_AMOUNT = 60000;
  public static final String UNAUTH_CVV = "456";

  public HealthService(PaymentsRepository paymentsRepository,
      AcquirerClient acquirerClient) {
    this.paymentsRepository = paymentsRepository;
    this.acquirerClient = acquirerClient;
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
      var dummyPayment = new AcquirerRequest(
          UNAUTH_PAN,
          UNAUTH_DATE,
          UNAUTH_CURRENCY,
          UNAUTH_AMOUNT,
          UNAUTH_CVV
      );
      var response = acquirerClient.processPayment(dummyPayment);
      if (response == null || response.getAuthorized()) {
        return false;
      }
    } catch (Exception e) {
      LOG.error("Error while checking Acquirer health", e);
      return false;
    }
    return true;
  }

}
