package com.udea.banktdea.repository;

import com.udea.banktdea.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for managing {@link Transaction} entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Retrieves all transactions ordered by timestamp descending (newest first).
     *
     * @return List of all historical transactions
     */
    List<Transaction> findAllByOrderByTimestampDesc();

    /**
     * Retrieves transactions related to a specific account (either as source or destination).
     *
     * @param sourceAccount      Source account number
     * @param destinationAccount Destination account number
     * @return List of transactions ordered descending
     */
    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumberOrderByTimestampDesc(
            String sourceAccount, String destinationAccount);

    /**
     * Looks up a transaction by its unique reference code.
     *
     * @param transactionReference Reference string (e.g. TRX-...)
     * @return Optional containing transaction
     */
    Optional<Transaction> findByTransactionReference(String transactionReference);
}

