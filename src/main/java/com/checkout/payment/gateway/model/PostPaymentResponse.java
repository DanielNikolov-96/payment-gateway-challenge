package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import lombok.Builder;
import java.util.UUID;

@Builder
public record PostPaymentResponse(UUID id, PaymentStatus status, String cardNumberLastFour, String expiryMonth, int expiryYear,
                                  String currency, long amount) {
  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }
}
