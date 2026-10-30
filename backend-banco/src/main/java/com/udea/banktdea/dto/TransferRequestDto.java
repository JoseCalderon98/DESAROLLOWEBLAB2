package com.udea.banktdea.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Data Transfer Object record capturing a fund transfer request.
 *
 * @param sourceAccountNumber      Account sending the money
 * @param destinationAccountNumber Account receiving the money
 * @param amount                   Monetary amount to transfer
 * @param description              Optional reference note
 */
public record TransferRequestDto(
    @NotBlank(message = "Source account number is required")
    String sourceAccountNumber,

    @NotBlank(message = "Destination account number is required")
    String destinationAccountNumber,

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.01", message = "Transfer amount must be strictly greater than 0")
    BigDecimal amount,

    String description
) {
}
