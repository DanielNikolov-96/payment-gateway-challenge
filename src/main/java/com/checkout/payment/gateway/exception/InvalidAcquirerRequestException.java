package com.checkout.payment.gateway.exception;

public class InvalidAcquirerRequestException extends RuntimeException {
  public InvalidAcquirerRequestException(String message) {
    super(message);
  }
}
