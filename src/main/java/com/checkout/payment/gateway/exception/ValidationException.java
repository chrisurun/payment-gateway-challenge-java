package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.util.FieldError;
import java.util.ArrayList;

public class ValidationException extends RuntimeException {

  ArrayList<FieldError> fieldErrors;

  public ValidationException(String message, ArrayList<FieldError> fieldErrors) {
    super(message);
    this.fieldErrors = fieldErrors;
  }

  public ArrayList<FieldError> getFieldErrors() {
    return fieldErrors;
  }
}
