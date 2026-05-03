package com.checkout.payment.gateway.exception;

public class InvalidPaymentIdException extends RuntimeException{
  public InvalidPaymentIdException(String message) {
    super(message);
  }
}
