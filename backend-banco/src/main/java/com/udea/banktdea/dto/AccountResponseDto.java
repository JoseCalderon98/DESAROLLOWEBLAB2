package com.udea.banktdea.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object record representing a bank account summary.
 *
 * @param id            Unique account identifier
 * @param accountNumber Account number string
 * @param accountHolder Name of the account holder
 * @param balance       Current available balance
 * @param accountType   Account category (AHORROS / CORRIENTE)
 * @param status        Account status (ACTIVA / BLOQUEADA)
 */
public record AccountResponseDto(
    Long id,
    String accountNumber,
    String accountHolder,
    BigDecimal balance,
    String accountType,
    String status
) {
}
