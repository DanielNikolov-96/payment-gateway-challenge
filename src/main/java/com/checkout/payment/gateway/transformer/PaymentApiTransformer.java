package com.checkout.payment.gateway.transformer;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PaymentApiTransformer {

  public PostPaymentResponse toRejectedPaymentResponse(PostPaymentRequest paymentRequest) {
    return toClientJson(paymentRequest, PaymentStatus.REJECTED);
  }

  public PostPaymentResponse toClientJson(PostPaymentRequest normalized, PaymentStatus paymentStatus) {
    String cardNumber = normalized.cardNumber();
    String cardLastFour = cardNumber.substring(cardNumber.length() - 4);
    return PostPaymentResponse.builder()
        .id(UUID.randomUUID())
        .status(paymentStatus)
        .cardNumberLastFour(cardLastFour)
        .expiryMonth(normalized.expiryMonth())
        .expiryYear(normalized.expiryYear())
        .currency(normalized.currency())
        .amount(normalized.amount())
        .build();
  }

  public PostPaymentRequest normalize(PostPaymentRequest request) {
    return PostPaymentRequest.builder()
        .cardNumber(request.cardNumber().trim())
        .expiryMonth(normalizeExpiryMonth(request.expiryMonth()))
        .expiryYear(request.expiryYear())
        .currency(request.currency().trim().toUpperCase())
        .amount(request.amount())
        .cvv(request.cvv().trim())
        .build();
  }

  private String normalizeExpiryMonth(String expiryMonth) {
    if (expiryMonth == null) {
      return null;
    }

    String month = expiryMonth.trim();
    if (month.length() == 1) {
      month = "0" + month;
    }
    return month;
  }
}
