package com.checkout.payment.gateway.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.now;
import static java.util.Currency.getInstance;

public class Validator {

  private final ArrayList<FieldError> errors = new ArrayList<>() {
  };

  public ArrayList<FieldError> getErrors() {
    return this.errors;
  }

  public boolean isValid() {
    return this.errors.isEmpty();
  }

  public void validateCurrency(String currencyCode) {
    try {
      getInstance(currencyCode);
    } catch (Exception e) {
      this.errors.add(new FieldError("currency", "Invalid currency"));
    }
  }

  public void validateCardNumber(String cardNumber) {
    if (cardNumber == null || cardNumber.length() < 14 || cardNumber.length() > 19) {
      this.errors.add(new FieldError("cardNumber", "Invalid card number length"));
      return;
    }
    // https://medium.com/@veerujadhav879/
    int sum = 0;
    boolean alternate = false;
    for (var i = cardNumber.length() - 1; i >= 0; i--) {
      int n = parseInt(cardNumber.substring(i, i + 1));
      if (alternate) {
        n *= 2;
        if (n > 9) {
          n -= 9;
        }
      }
      sum += n;
      alternate = !alternate;
    }
    if (sum % 10 == 0) {
      return;
    }
    errors.add(new FieldError("cardNumber", "Invalid card number"));
  }

  public void validateCardExpiry(int expiryMonth, int expiryYear) {
    var hasError = false;
    var now = now();
    if (expiryMonth < 1 || expiryMonth > 12) {
      this.errors.add(new FieldError("expiryMonth", "Invalid expiry month"));
      hasError = true;
    }
    if (expiryYear < now.getYear() || expiryYear > 2099) {
      errors.add(new FieldError("expiryYear", "Invalid expiry year"));
      hasError = true;
    }
    if (hasError) {
      return;
    }
    var cardDate = LocalDateTime.of(expiryYear, expiryMonth, 1, 0, 0).plusMonths(1).minusDays(1);
    if (now.isAfter(cardDate)) {
      errors.add(new FieldError("expiryMonth", "Card has expired"));
    }
  }

  public void validateCardCvv(String cardCvv) {
    if (cardCvv == null || cardCvv.length() < 3 || cardCvv.length() > 4) {
      this.errors.add(new FieldError("cvv", "Invalid CVV length"));
    }
  }

  public void validateAmount(int amount) {
    if (amount < 1) {
      this.errors.add(new FieldError("amount", "Invalid amount"));
    }
  }

}
