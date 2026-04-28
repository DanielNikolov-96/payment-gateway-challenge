package com.checkout.payment.gateway.validation;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.ZoneId;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, PostPaymentRequest> {

  @Override
  public boolean isValid(PostPaymentRequest request, ConstraintValidatorContext context) {
    try {
      if (request.getExpiryYear() < 0) {
        throw new IllegalArgumentException("Expected expiryYear to be a positive number but was " + request.getExpiryYear());
      }

      YearMonth expirationDate = YearMonth.of(request.getExpiryYear(), toNumericMonth(request.getExpiryMonth()));
      YearMonth now = YearMonth.now(ZoneId.of("UTC"));

      if (expirationDate.isBefore(now)) {
        throw new IllegalArgumentException(
            "Expected expirationDate date in the future but was " + expirationDate);
      }
      return true;
    } catch (Exception e) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(e.getMessage())
          .addConstraintViolation();
      return false;
    }
  }

  private int toNumericMonth(String expiryMonth) {
    String expMonth = expiryMonth.trim();
    if (!expMonth.matches("^\\d{1,2}$")) {
      throw new IllegalArgumentException("Expected expiryMonth to be a number between 1 and 12 but was " + expMonth);
    }
    if (expMonth.startsWith("0")) {
      expMonth = expMonth.substring(1);
    }
    int month = Integer.parseInt(expMonth);
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Expected expiryMonth to be a number between 1 and 12 but was " + expMonth);
    }
    return month;
  }
}