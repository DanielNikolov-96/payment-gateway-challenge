package com.checkout.payment.gateway.model;

public record AcquirerResponse(
    boolean authorized,
    String authorizationCode
) {

  public AcquirerResponse(boolean authorized) {
    this(authorized, null);
  }

  @Override
  public String toString() {
    return "AcquirerResponse{" +
        "authorized=" + authorized +
        ", authorizationCode=" + "masked" +
        '}';
  }
}
