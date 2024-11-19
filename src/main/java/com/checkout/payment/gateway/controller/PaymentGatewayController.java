package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.exception.ValidationException;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;
import com.checkout.payment.gateway.util.Validator;
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

  @GetMapping("/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }

  private Validator validatedPaymentRequest(Validator valid, PostPaymentRequest paymentRequest) {
    valid.validateAmount(paymentRequest.getAmount());
    valid.validateCardNumber(paymentRequest.getCardNumber());
    valid.validateCurrency(paymentRequest.getCurrency());
    valid.validateCardCvv(paymentRequest.getCvv());
    valid.validateCardExpiry(paymentRequest.getExpiryMonth(), paymentRequest.getExpiryYear());
    return valid;
  }

  @PostMapping("")
  public ResponseEntity<Object> processPayment(
      @RequestBody PostPaymentRequest paymentRequest) {
    var valid = validatedPaymentRequest(new Validator(), paymentRequest);
    if (!valid.isValid()) {
      throw new ValidationException("Invalid payment request", valid.getErrors());
    }
    return new ResponseEntity<>(
        paymentGatewayService.processPayment(paymentRequest),
        HttpStatus.OK);
  }
}
