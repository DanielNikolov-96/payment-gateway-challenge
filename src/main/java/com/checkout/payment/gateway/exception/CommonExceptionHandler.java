package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  @ExceptionHandler(InvalidPaymentIdException.class)
  public ResponseEntity<ErrorResponse> handleInvalidIdException(InvalidPaymentIdException ex) {
    LOG.error("Invalid ID failure due to: ", ex);
    return new ResponseEntity<>(new ErrorResponse("Page not found"),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ExternalFailureException.class)
  public ResponseEntity<ErrorResponse> handleExternalFailureException(ExternalFailureException ex) {
    LOG.error("Request to external system failed due to: ", ex);
    return new ResponseEntity<>(new ErrorResponse("Your payment could not be processed by an external component, please try again later"),
        HttpStatus.GATEWAY_TIMEOUT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    LOG.error("Following exception occurred: ", ex);
    return ResponseEntity.internalServerError().body("Internal server error, please try again later");
  }
}
