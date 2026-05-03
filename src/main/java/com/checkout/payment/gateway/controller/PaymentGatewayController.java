package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;
import com.checkout.payment.gateway.validator.PaymentValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentGatewayController {

  private final PaymentValidator validator;
  private final PaymentGatewayService service;

  public PaymentGatewayController(PaymentValidator validator, PaymentGatewayService service) {
    this.validator = validator;
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<PostPaymentResponse> makePayment(@RequestBody PostPaymentRequest request) {
    if (!validator.isValid(request)) {
      return ResponseEntity.ok(service.processRejectedPayment(request));
    }
    return ResponseEntity.ok(service.processPayment(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostPaymentResponse> getPayment(@PathVariable UUID id) {
    return new ResponseEntity<>(service.getPaymentById(id), HttpStatus.OK);
  }
}
