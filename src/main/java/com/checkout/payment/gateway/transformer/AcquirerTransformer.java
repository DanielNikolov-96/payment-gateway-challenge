package com.checkout.payment.gateway.transformer;

import com.checkout.payment.gateway.model.AcquirerRequest;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.springframework.stereotype.Component;

@Component
public class AcquirerTransformer {

  private static final String YEAR_MONTH_DELIMITER = "/";

  public AcquirerRequest toAcquirerRequest(PostPaymentRequest request) {
    return AcquirerRequest.builder()
        .cardNumber(request.cardNumber())
        .expiryDate(toExpiryDate(request.expiryMonth(), request.expiryYear()))
        .currency(request.currency())
        .amount(request.amount())
        .cvv(request.cvv())
        .build();
  }

  private String toExpiryDate(String expiryMonth, Integer expiryYear) {
    return String.join(YEAR_MONTH_DELIMITER, expiryMonth, expiryYear.toString());
  }
}
