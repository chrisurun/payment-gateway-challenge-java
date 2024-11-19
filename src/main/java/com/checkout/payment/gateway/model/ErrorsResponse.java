package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.util.FieldError;
import java.util.ArrayList;

public class ErrorsResponse extends ErrorResponse {

  private final ArrayList<FieldError> errors;

  public ErrorsResponse(String message, ArrayList<FieldError> errors) {
    super(message);
    this.errors = errors;
  }

  public ArrayList<FieldError> getErrors() {
    return errors;
  }
}
