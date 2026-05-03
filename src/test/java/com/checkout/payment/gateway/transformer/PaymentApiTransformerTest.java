package com.checkout.payment.gateway.transformer;

import com.checkout.payment.gateway.common.TestUtils;
import com.checkout.payment.gateway.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentApiTransformerTest {

  @Autowired
  private PaymentApiTransformer transformer;

  @Test
  void whenSingleDigitMonthThenNormalizeToDoubleDigits() {
    var request = TestUtils.buildPaymentRequest(PaymentStatus.AUTHORIZED)
        .expiryMonth("   2   ").build();

    var result = transformer.normalize(request);

    assert result.expiryMonth().equals("02");
  }

  @Test
  void whenDoubleDigitMonthThenNormalizeToDoubleDigits() {
    var request = TestUtils.buildPaymentRequest(PaymentStatus.AUTHORIZED)
        .expiryMonth("   12   ").build();

    var result = transformer.normalize(request);

    assert result.expiryMonth().equals("12");
  }

  @Test
  void whenCurrencyInLowerCaseThenNormalizeToUpperCase() {
    var request = TestUtils.buildPaymentRequest(PaymentStatus.AUTHORIZED)
        .currency("   usd   ").build();
    var result = transformer.normalize(request);

    assert result.currency().equals("USD");
  }
}
