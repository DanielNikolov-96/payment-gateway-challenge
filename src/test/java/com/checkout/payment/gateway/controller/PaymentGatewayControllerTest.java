package com.checkout.payment.gateway.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.common.TestUtils;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  private static final String UNAVAILABLE_ACQUIRER_STUB = "0";

  @Autowired
  private MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse(
        UUID.randomUUID(), PaymentStatus.AUTHORIZED, "4321", "12",
        2024, "USD", 10);

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.id()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.status").value(payment.status().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.cardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.expiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.expiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.currency()))
        .andExpect(jsonPath("$.amount").value(payment.amount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Page not found"));
  }

  @Test
  void whenValidPaymentRequestThenPaymentIsProcessed() throws Exception {
    PostPaymentRequest validRequest = TestUtils.buildPaymentRequest(PaymentStatus.AUTHORIZED).build();

    MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/payment")
            .contentType("application/json")
            .content(TestUtils.asJsonString(validRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.status").value(PaymentStatus.AUTHORIZED.getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(TestUtils.getCardNumberLastFour(validRequest.cardNumber())))
        .andExpect(jsonPath("$.expiryMonth").value(validRequest.expiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(validRequest.expiryYear()))
        .andExpect(jsonPath("$.currency").value(validRequest.currency()))
        .andExpect(jsonPath("$.amount").value(validRequest.amount()))
        .andReturn().getResponse();
    assertThat(paymentsRepository.get(TestUtils.getUUid(response)).isPresent());
  }

  @Test
  void whenShouldDeclinePaymentThenPaymentIsDeclined() throws Exception {
    PostPaymentRequest validRequest = TestUtils.buildPaymentRequest(PaymentStatus.DECLINED).build();

    MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/payment")
            .contentType("application/json")
            .content(TestUtils.asJsonString(validRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.status").value(PaymentStatus.DECLINED.getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(TestUtils.getCardNumberLastFour(validRequest.cardNumber())))
        .andExpect(jsonPath("$.expiryMonth").value(validRequest.expiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(validRequest.expiryYear()))
        .andExpect(jsonPath("$.currency").value(validRequest.currency()))
        .andExpect(jsonPath("$.amount").value(validRequest.amount()))
        .andReturn().getResponse();
    assertThat(paymentsRepository.get(TestUtils.getUUid(response)).isPresent());
  }

  @Test
  void whenInvalidPaymentDataPaymentIsRejected() throws Exception {
    PostPaymentRequest invalidRequest = TestUtils
        .buildPaymentRequest(null)
        .currency(null)
        .build();

    MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/payment")
            .contentType("application/json")
            .content(TestUtils.asJsonString(invalidRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.status").value(PaymentStatus.REJECTED.getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(TestUtils.getCardNumberLastFour(invalidRequest.cardNumber())))
        .andExpect(jsonPath("$.expiryMonth").value(invalidRequest.expiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(invalidRequest.expiryYear()))
        .andExpect(jsonPath("$.currency").value(invalidRequest.currency()))
        .andExpect(jsonPath("$.amount").value(invalidRequest.amount()))
        .andReturn().getResponse();
    assertThat(paymentsRepository.get(TestUtils.getUUid(response)).isPresent());
  }

  @Test
  void whenAcquirerIsUnavailableThenGatewayTimeoutResponse() throws Exception {
    PostPaymentRequest request = TestUtils
        .buildPaymentRequest(null)
        .cardNumber("1".repeat(14) + UNAVAILABLE_ACQUIRER_STUB)
        .build();

    mvc.perform(MockMvcRequestBuilders.post("/payment")
            .contentType("application/json")
            .content(TestUtils.asJsonString(request)))
        .andExpect(status().isGatewayTimeout())
        .andExpect(jsonPath("$.message").value("Your payment could not be processed by an external component, please try again later"));
  }
}
