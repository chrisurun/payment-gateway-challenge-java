package com.checkout.payment.gateway.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static java.lang.Integer.parseInt;
import static java.time.LocalDateTime.now;
import static java.util.Currency.getInstance;
import static java.util.UUID.fromString;

public class Validator {

  public static String INVALID_CURRENCY = "Invalid currency";
  public static String INVALID_CARD_NUMBER = "Invalid card number";
  public static String INVALID_EXPIRY_MONTH = "Invalid expiry month";
  public static String INVALID_EXPIRY_YEAR = "Invalid expiry year";
  public static String CARD_EXPIRED = "Card is expired";
  public static String INVALID_CVV = "Invalid CVV";
  public static String INVALID_AMOUNT = "Invalid amount";

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
      this.errors.add(new FieldError("currency", INVALID_CURRENCY));
    }
  }

  public void validateCardNumber(String cardNumber) {
    if (cardNumber == null || cardNumber.length() < 14 || cardNumber.length() > 19) {
      this.errors.add(new FieldError("cardNumber", INVALID_CARD_NUMBER));
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
    errors.add(new FieldError("cardNumber", INVALID_CARD_NUMBER));
  }

  public void validateCardExpiry(int expiryMonth, int expiryYear) {
    var hasError = false;
    var now = now();
    if (expiryMonth < 1 || expiryMonth > 12) {
      this.errors.add(new FieldError("expiryMonth", INVALID_EXPIRY_MONTH));
      hasError = true;
    }
    if (expiryYear < 2000 || expiryYear > 2099) {
      errors.add(new FieldError("expiryYear", INVALID_EXPIRY_YEAR));
      hasError = true;
    }
    if (hasError) {
      return;
    }
    var cardDate = LocalDateTime.of(expiryYear, expiryMonth, 1, 0, 0).plusMonths(1).minusDays(1);
    if (now.isAfter(cardDate)) {
      errors.add(new FieldError("expiryDate", CARD_EXPIRED));
    }
  }

  public void validateCardCvv(String cardCvv) {
    if (cardCvv == null || cardCvv.length() < 3 || cardCvv.length() > 4) {
      this.errors.add(new FieldError("cvv", INVALID_CVV));
    }
  }

  public void validateAmount(int amount) {
    if (amount < 1) {
      this.errors.add(new FieldError("amount", INVALID_AMOUNT));
    }
  }

  public UUID validateUuid(String uuid) {
    try {
      return fromString(uuid);
    } catch (Exception e) {
      this.errors.add(new FieldError("id", "Invalid UUID"));
    }
    return null;
  }

}
