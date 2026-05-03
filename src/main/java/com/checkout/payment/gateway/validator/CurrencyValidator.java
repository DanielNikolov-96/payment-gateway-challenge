package com.checkout.payment.gateway.validator;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import java.util.Set;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, PostPaymentRequest> {

  private static final Set<String> SUPPORTED_CURRENCIES = Set.of(
      "USD",
      "EUR"
  );

  @Override
  public boolean isValid(PostPaymentRequest request, ConstraintValidatorContext context) {
    try {
      if (StringUtils.isBlank(request.currency())) {
        throw new IllegalArgumentException("Expected currency to be non-empty but was " + request.currency());
      }

      String currency = request.currency().trim().toUpperCase();
      if (!SUPPORTED_CURRENCIES.contains(currency)) {
        throw new IllegalArgumentException(String.format("Expected currency to be one of %s but was %s",
            SUPPORTED_CURRENCIES, request.currency()));
      }
      return true;
    } catch (Exception e) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.getMessage())
          .addConstraintViolation();
      return false;
    }
  }

}
