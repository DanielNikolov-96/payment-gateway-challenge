package com.checkout.payment.gateway.validator;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseValidationTest {

  private static final int CARD_NUM_MIN_LEN = 14;

  Validator validator;

  @BeforeEach
  void setup() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  protected PostPaymentRequest validRequest() {
    PostPaymentRequest req = new PostPaymentRequest();
    req.setCardNumber(String.format(" %s ", "1".repeat(CARD_NUM_MIN_LEN)));
    req.setExpiryMonth(" 02 ");
    req.setExpiryYear(2099);
    req.setCurrency(" USD ");
    req.setAmount(0L);
    req.setCvv(" 123 ");
    return req;
  }
}