package com.udea.banktdea.dto;

import com.udea.banktdea.entity.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object record representing the completed transaction receipt.
 *
 * @param id                       Unique transaction ID
 * @param transactionReference     Unique alphanumeric reference
 * @param sourceAccountNumber      Source account
 * @param destinationAccountNumber Destination account
 * @param amount                   Transfer amount
 * @param timestamp                Execution timestamp
 * @param status                   Result status (EXITOSA, etc.)
 * @param description              Optional note
 * @param qrCodeData               Generated QR code SVG or Base64 data representation
 */
public record TransactionResponseDto(
    Long id,
    String transactionReference,
    String sourceAccountNumber,
    String destinationAccountNumber,
    BigDecimal amount,
    LocalDateTime timestamp,
    TransactionStatus status,
    String description,
    String qrCodeData
) {
}

