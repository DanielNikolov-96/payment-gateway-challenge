package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.validation.ValidExpiryDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import java.io.Serializable;

@ValidExpiryDate
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy.class)
public class PostPaymentRequest implements Serializable {

  @NotBlank(message = "Expected non-empty cardNumber but none passed")
  @Pattern(regexp = "^\\s*\\d{14,19}\\s*$", message = "Expected 14-19 digit card number but was not")
  private String cardNumber;

  @NotNull(message = "Expected expiryMonth but none passed")
  private String expiryMonth;

  @NotNull(message = "Expected expiryYear but none passed")
  private Integer expiryYear;

  @NotBlank(message = "Expected currency but none passed")
  @Pattern(regexp = "^\\s*(USD|usd|EUR|eur)\\s*$", message = "Expected currency to be USD or EUR but was not")
  private String currency;

  @NotNull(message = "Expected amount but none passed")
  @Min(value = 0, message = "Expected amount >= 0 but was negative")
  private Long amount;

  @NotBlank(message = "Expected cvv but none passed")
  @Pattern(regexp = "^\\s*\\d{3,4}\\s*", message = "Expected 3-4 digit cvv but was not")
  private String cvv;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(String expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public Integer getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(Integer expiryYear) { this.expiryYear = expiryYear; }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }
}