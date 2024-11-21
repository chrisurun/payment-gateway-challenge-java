package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.model.HealthResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.UUID;

public class GatewayClient extends BaseClient {

  private static final String HEALTH = "/health";
  private static final String PAYMENT = "/payment";
  private static final String PAYMENT_ = PAYMENT + "/";

  public GatewayClient(String baseUri, String port) {
    super(baseUri, port);
  }

  public HealthResponse getHealth() {
    var response = get(HEALTH, HealthResponse.class);
    return (HealthResponse) response;
  }

  public PostPaymentResponse getPayment(UUID id) {
    var response = get(PAYMENT_ + id.toString(), PostPaymentResponse.class);
    return (PostPaymentResponse) response;
  }

  public PostPaymentResponse processPayment(PostPaymentRequest request) {
    var response = post(PAYMENT, request, PostPaymentResponse.class);
    return (PostPaymentResponse) response;
  }

}
