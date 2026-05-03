package com.checkout.payment.gateway.common;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.CollectionUtils;
import java.io.UnsupportedEncodingException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TestUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Random RANDOM = new Random();
  private static final int CARD_NUM_MIN_LEN = 14;

  private static final EnumMap<PaymentStatus, List<Integer>> STATUS_TO_CARD_NUMBER_MAP = new EnumMap<>(Map.of(
      PaymentStatus.AUTHORIZED, List.of(1, 3, 5, 7, 9),
      PaymentStatus.DECLINED, List.of(2, 4, 6, 8)));

  public static PostPaymentRequest.PostPaymentRequestBuilder buildPaymentRequest(PaymentStatus status) {
    String lastCardDigit = status == null ? "" : String.valueOf(getLastCardDigit(status));

    return PostPaymentRequest.builder()
        .cardNumber("1".repeat(CARD_NUM_MIN_LEN) + lastCardDigit)
        .expiryMonth("12")
        .expiryYear(3099)
        .currency("USD")
        .amount(100L)
        .cvv("123");
  }

  private static int getLastCardDigit(PaymentStatus status) {
    List<Integer> validDigits = STATUS_TO_CARD_NUMBER_MAP.get(status);
    if (CollectionUtils.isEmpty(validDigits)) {
      throw new IllegalArgumentException("No valid card digits defined for status: " + status);
    }
    return validDigits.get(RANDOM.nextInt(validDigits.size()));
  }

  public static String asJsonString(PostPaymentRequest request) throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(request);
  }

  public static String getCardNumberLastFour(String cardNumber) {
    return cardNumber.substring(cardNumber.length() - 4);
  }

  public static UUID getUUid(MockHttpServletResponse response)
      throws UnsupportedEncodingException, JsonProcessingException {
    String content = response.getContentAsString();
    PostPaymentResponse paymentResponse = OBJECT_MAPPER.readValue(content, PostPaymentResponse.class);
    return paymentResponse.id();
  }
}
