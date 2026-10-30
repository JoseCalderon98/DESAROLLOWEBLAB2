package com.udea.banktdea.repository;

import com.udea.banktdea.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for managing {@link Account} entities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by its unique account number.
     *
     * @param accountNumber The account number
     * @return Optional containing the account if found
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Checks if an account exists by its account number.
     *
     * @param accountNumber The account number
     * @return true if exists
     */
    boolean existsByAccountNumber(String accountNumber);
}

