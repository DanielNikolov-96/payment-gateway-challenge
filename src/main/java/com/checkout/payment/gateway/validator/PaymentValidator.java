package com.checkout.payment.gateway.validator;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PaymentValidator {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentValidator.class);

  private final Validator validator;

  public PaymentValidator(Validator validator) {
    this.validator = validator;
  }

  public boolean isValid(PostPaymentRequest request) {
    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(request);
    String errorMessage = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(";"));
    if (!violations.isEmpty()) {
      LOG.error("Payment request validation failed with error: {} for request: {}", errorMessage, request);
    }
    return violations.isEmpty();
  }

}
