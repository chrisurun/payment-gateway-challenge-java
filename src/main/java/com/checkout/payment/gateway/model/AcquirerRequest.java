package com.checkout.payment.gateway.model;

public class AcquirerRequest {

  private String card_number;
  private String expiry_date;
  private String currency;
  private int amount;
  private int cvv;

  public AcquirerRequest(
      String cardNumber,
      String expiryDate,
      String currency,
      int amount,
      int cvv) {
    this.card_number = cardNumber;
    this.expiry_date = expiryDate;
    this.currency = currency;
    this.amount = amount;
    this.cvv = cvv;
  }

  public String getCardNumber() {
    return card_number;
  }

  public void setCardNumber(String cardNumber) {
    this.card_number = cardNumber;
  }

  public String getExpiryDate() {
    return expiry_date;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiry_date = expiryDate;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getCvv() {
    return cvv;
  }

  public void setCvv(int cvv) {
    this.cvv = cvv;
  }
}
