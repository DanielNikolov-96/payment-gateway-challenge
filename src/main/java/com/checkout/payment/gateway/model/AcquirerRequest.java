package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AcquirerRequest(
    @JsonProperty("card_number") String cardNumber,
    @JsonProperty("expiry_date") String expiryDate,
    String currency,
    long amount,
    String cvv
) {

  @Override
  public String toString() {
    return "AcquirerRequest{" +
        "cardNumber=masked" +
        ", expiryDate='" + expiryDate + '\'' +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=masked" +
        '}';
  }
}
