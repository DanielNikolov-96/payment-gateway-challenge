package com.checkout.payment.gateway.exception;

public class ExternalFailureException extends RuntimeException {
  public ExternalFailureException(String message) {
    super(message);
  }
}
