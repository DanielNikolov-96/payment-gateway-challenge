package com.checkout.payment.gateway.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
public @interface ValidCurrency {

  String message() default "Invalid currency";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}