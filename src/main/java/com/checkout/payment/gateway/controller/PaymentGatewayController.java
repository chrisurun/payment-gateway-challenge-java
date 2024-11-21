package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.exception.ValidationException;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import com.checkout.payment.gateway.util.Validator;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;

  public PaymentGatewayController(PaymentGatewayService paymentGatewayService) {
    this.paymentGatewayService = paymentGatewayService;
  }

  private Validator validatedPaymentRequest(Validator valid, PostPaymentRequest paymentRequest) {
    valid.validateAmount(paymentRequest.getAmount());
    // Remove following check for declined test case
    valid.validateCardNumber(paymentRequest.getCardNumber());
    valid.validateCurrency(paymentRequest.getCurrency());
    valid.validateCardCvv(paymentRequest.getCvv());
    valid.validateCardExpiry(paymentRequest.getExpiryMonth(), paymentRequest.getExpiryYear());
    return valid;
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable("id")
  @Parameter(
      name = "id",
      description = "Payment id",
      example = "19f3c9f1-1384-45c2-ac36-b41f90111446") String id) {
    var valid = new Validator();
    var uuid = valid.validateUuid(id);
    if (!valid.isValid()) {
      throw new ValidationException("Invalid get payment request", valid.getErrors());
    }
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(uuid), HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<PostPaymentResponse> processPayment(
      @RequestBody PostPaymentRequest paymentRequest) {
    var valid = validatedPaymentRequest(new Validator(), paymentRequest);
    if (!valid.isValid()) {
      throw new ValidationException("Invalid create payment request", valid.getErrors());
    }
    return new ResponseEntity<>(
        paymentGatewayService.processPayment(paymentRequest),
        HttpStatus.OK);
  }
}
