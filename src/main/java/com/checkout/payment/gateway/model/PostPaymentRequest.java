package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.validator.ValidCurrency;
import com.checkout.payment.gateway.validator.ValidExpiryDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.Builder;

@ValidExpiryDate
@ValidCurrency
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy.class)
@Builder
public record PostPaymentRequest(
    @NotBlank(message = "Expected non-empty cardNumber but none passed")
    @Pattern(regexp = "^\\s*\\d{14,19}\\s*$", message = "Expected 14-19 digit card number but was not")
    String cardNumber,

    @NotBlank(message = "Expected expiryMonth but none passed")
    String expiryMonth,

    @NotNull(message = "Expected expiryYear but none passed")
    Integer expiryYear,

    @NotBlank(message = "Expected currency but none passed")
    String currency,

    @NotNull(message = "Expected amount but none passed")
    @Min(value = 0, message = "Expected amount >= 0 but was negative")
    Long amount,

    @NotBlank(message = "Expected cvv but none passed")
    @Pattern(regexp = "^\\s*\\d{3,4}\\s*", message = "Expected 3-4 digit cvv but was not")
    String cvv
) {

    @Override
    public String toString() {
        return "PostPaymentRequest{" +
            "cardNumber=masked" +
            ", expiryMonth='" + expiryMonth + '\'' +
            ", expiryYear=" + expiryYear +
            ", currency='" + currency + '\'' +
            ", amount=" + amount +
            ", cvv='masked'" +
            '}';
    }

}