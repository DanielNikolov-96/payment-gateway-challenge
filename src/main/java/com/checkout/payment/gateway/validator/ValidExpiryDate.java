package com.checkout.payment.gateway.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExpiryDateValidator.class)
public @interface ValidExpiryDate {

  String message() default "Invalid expiry date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}