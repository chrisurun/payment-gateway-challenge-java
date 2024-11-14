package com.checkout.payment.gateway.client;

import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.AcquirerResponse;

public class AcquirerClient extends BaseClient {

  private static final String PAYMENTS = "/payments";

  public AcquirerClient(String baseUri, String port) {
    super(baseUri, port);
  }

  public AcquirerResponse processPayment(AcquirerRequest request) {
    var response = post(PAYMENTS, request, AcquirerResponse.class);
    return (AcquirerResponse) response;
  }

}
