package com.checkout.payment.gateway.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.YearMonth;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentValidationTest extends BaseValidationTest {

  @Test
  void shouldPassValidationForValidRequest() {
    var violations = validator.validate(validRequest());
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"\t", "not a number", "123", "12345678901234567890"})
  void shouldFailWhenCardNumberInvalid(String invalidCardNumber) {
    var request = validRequest()
        .cardNumber(invalidCardNumber)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"\t", "not a number", "0", "13"})
  void shouldFailWhenExpiryMonthInvalid(String invalidExpiryMonth) {
    var request = validRequest()
        .expiryMonth(invalidExpiryMonth)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void shouldFailWhenExpiryYearInvalid(Integer invalidExpiryYear) {
    var request = validRequest()
        .expiryYear(invalidExpiryYear)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenFreshlyExpiredCard() {
    YearMonth now = YearMonth.now(ZoneId.of("UTC"));
    YearMonth freshlyExpired = now.minusMonths(1);

    var request = validRequest()
        .expiryMonth(String.valueOf(freshlyExpired.getMonthValue()))
        .expiryYear(freshlyExpired.getYear())
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "\t", "not a currency"})
  void shouldFailWhenCurrencyInvalid(String invalidCurrency) {
    var request = validRequest()
        .currency(invalidCurrency)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenAmountInvalid() {
    var request = validRequest()
        .amount(-1L)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"\t", "not a number", "1", "12345"})
  void shouldFailWhenCVVInvalid(String invalidCvv) {
    var request = validRequest()
        .cvv(invalidCvv)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCardNumber() {
    var request = validRequest()
        .cardNumber(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoExpiryMonth() {
    var request = validRequest()
        .expiryMonth(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoExpiryYear() {
    var request = validRequest()
        .expiryYear(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCurrency() {
    var request = validRequest()
        .currency(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoAmount() {
    var request = validRequest()
        .amount(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCvv() {
    var request = validRequest()
        .cvv(null)
        .build();

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }
}
