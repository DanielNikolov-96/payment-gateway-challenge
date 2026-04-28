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
    var request = validRequest();
    request.setCardNumber(invalidCardNumber);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"\t", "not a number", "0", "13"})
  void shouldFailWhenExpiryMonthInvalid(String invalidExpiryMonth) {
    var request = validRequest();
    request.setExpiryMonth(invalidExpiryMonth);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void shouldFailWhenExpiryYearInvalid(Integer invalidExpiryYear) {
    var request = validRequest();
    request.setExpiryYear(invalidExpiryYear);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenFreshlyExpiredCard() {
    var request = validRequest();
    YearMonth now = YearMonth.now(ZoneId.of("UTC"));
    YearMonth freshlyExpired = now.minusMonths(1);

    request.setExpiryMonth(String.valueOf(freshlyExpired.getMonth().getValue()));
    request.setExpiryYear(freshlyExpired.getYear());

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "\t", "not a currency"})
  void shouldFailWhenCurrencyInvalid(String invalidCurrency) {
    var request = validRequest();
    request.setCurrency(invalidCurrency);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenAmountInvalid() {
    var request = validRequest();
    request.setAmount(-1L);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"\t", "not a number", "1", "12345"})
  void shouldFailWhenCVVInvalid(String invalidCvv) {
    var request = validRequest();
    request.setCvv(invalidCvv);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCardNumber() {
    var request = validRequest();
    request.setCardNumber(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoExpiryMonth() {
    var request = validRequest();
    request.setExpiryMonth(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoExpiryYear() {
    var request = validRequest();
    request.setExpiryYear(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCurrency() {
    var request = validRequest();
    request.setCurrency(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoAmount() {
    var request = validRequest();
    request.setAmount(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

  @Test
  void shouldFailWhenNoCvv() {
    var request = validRequest();
    request.setCvv(null);

    var violations = validator.validate(request);

    assertThat(violations).isNotEmpty();
  }

}
