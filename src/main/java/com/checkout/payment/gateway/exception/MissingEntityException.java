package com.checkout.payment.gateway.exception;

public class MissingEntityException extends RuntimeException {
  public MissingEntityException(String message) {
    super(message);
  }

}
