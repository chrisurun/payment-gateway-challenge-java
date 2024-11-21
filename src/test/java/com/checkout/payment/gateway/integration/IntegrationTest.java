package com.checkout.payment.gateway.integration;

import com.checkout.payment.gateway.BaseTest;
import com.checkout.payment.gateway.client.GatewayClient;

import static com.checkout.payment.gateway.util.Print.print;
import static com.checkout.payment.gateway.util.Print.printBlue;
import static com.checkout.payment.gateway.util.Print.printGreen;
import static com.checkout.payment.gateway.util.Print.section;
import static java.lang.String.valueOf;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest extends BaseTest {

  private final GatewayClient gatewayClient;

  public static void main(String[] args) {
    // With configuration:
//    var config = new Config();
//    var gatewayClient = new GatewayClient(
//        config.getProperty("gateway.baseUri"),
//        config.getProperty("gateway.port"));
    var gatewayClient = new GatewayClient("http://localhost", "8090");
    var integrationTest = new IntegrationTest(gatewayClient);
    integrationTest.all();
  }

  public IntegrationTest(GatewayClient gatewayClient) {
    this.gatewayClient = gatewayClient;
  }

  public void all() {
    assertNotNull(gatewayClient);
    health();
    payments();
  }

  public void health() {
    section("Health");
    printBlue("Get Health...");
    var health = gatewayClient.getHealth();
    print(valueOf(health));
    assertNotNull(health);
    assertTrue(health.acquirer());
    assertTrue(health.database());
    printGreen("Health check passed");
  }

  public void payments() {
    section("Payments");

    printBlue("Create Payment Request...");
    var createPaymentRequest = createAuthorisedPostPaymentRequest();
    var createPaymentResponse = gatewayClient.processPayment(createPaymentRequest);
    out.println(createPaymentResponse);
    assertNotNull(createPaymentResponse);
    assertEquals(createPaymentRequest.getAmount(), createPaymentResponse.getAmount());
    assertEquals(createPaymentRequest.getCurrency(), createPaymentResponse.getCurrency());
    var cardNumber = createPaymentRequest.getCardNumber();
    assertEquals(cardNumber.substring(cardNumber.length() - 4),
        createPaymentResponse.getCardNumberLastFour());
    printGreen("Payment request created");

    printBlue("Get Payment...");
    var paymentResponse = gatewayClient.getPayment(createPaymentResponse.getId());
    out.println(paymentResponse);
    assertNotNull(paymentResponse);
    assertEquals(createPaymentResponse.getId(), paymentResponse.getId());
    assertEquals(createPaymentResponse.getAmount(), paymentResponse.getAmount());
    assertEquals(createPaymentResponse.getCurrency(), paymentResponse.getCurrency());
    printGreen("Payment retrieved");
  }

}
