package com.udea.banktdea.config;

import com.udea.banktdea.entity.Account;
import com.udea.banktdea.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Initializes sample bank accounts on application startup if the repository is empty.
 */
@Configuration
public class DataInitializer {

    /**
     * Seeds initial demo accounts for immediate testing.
     *
     * @param accountRepository Account repository
     * @return CommandLineRunner callback
     */
    @Bean
    public CommandLineRunner initBankAccounts(AccountRepository accountRepository) {
        return args -> {
            if (accountRepository.count() == 0) {
                Account acc1 = new Account(
                        null,
                        "CTA-1001-8492",
                        "Diego Alejandro Ramírez",
                        new BigDecimal("7500000.00"),
                        "AHORROS",
                        "ACTIVA"
                );

                Account acc2 = new Account(
                        null,
                        "CTA-2002-3104",
                        "Pedro José Gómez",
                        new BigDecimal("3200000.00"),
                        "CORRIENTE",
                        "ACTIVA"
                );

                Account acc3 = new Account(
                        null,
                        "CTA-3003-9921",
                        "Valentina Franco Morales",
                        new BigDecimal("12400000.00"),
                        "AHORROS",
                        "ACTIVA"
                );

                Account acc4 = new Account(
                        null,
                        "CTA-4004-7715",
                        "Tecnológico de Antioquia - Tesorería",
                        new BigDecimal("45000000.00"),
                        "CORRIENTE",
                        "ACTIVA"
                );

                accountRepository.saveAll(List.of(acc1, acc2, acc3, acc4));
            }
        };
    }
}
