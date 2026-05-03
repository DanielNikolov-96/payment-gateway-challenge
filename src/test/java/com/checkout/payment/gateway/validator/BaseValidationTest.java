package com.checkout.payment.gateway.validator;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseValidationTest {

  private static final int CARD_NUM_MIN_LEN = 14;

  static Validator validator;

  @BeforeAll
  static void setup() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  protected PostPaymentRequest.PostPaymentRequestBuilder validRequest() {
    return PostPaymentRequest.builder()
        .cardNumber(String.format(" %s ", "1".repeat(CARD_NUM_MIN_LEN)))
        .expiryMonth(" 02 ")
        .expiryYear(3099)
        .currency(" USD ")
        .amount(0L)
        .cvv(" 123 ");
  }
}