package com.udea.banktdea.service;

import com.udea.banktdea.dto.AccountResponseDto;
import com.udea.banktdea.dto.TransactionResponseDto;
import com.udea.banktdea.dto.TransferRequestDto;
import com.udea.banktdea.entity.Account;
import com.udea.banktdea.entity.Transaction;
import com.udea.banktdea.entity.TransactionStatus;
import com.udea.banktdea.exception.BankingException;
import com.udea.banktdea.repository.AccountRepository;
import com.udea.banktdea.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Business service responsible for processing financial transfers and accounting operations.
 */
@Service
public class BankingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final QrCodeService qrCodeService;

    /**
     * Injects dependencies for repository operations and QR generation.
     *
     * @param accountRepository     Account JPA repository
     * @param transactionRepository Transaction JPA repository
     * @param qrCodeService         QR encoding service
     */
    public BankingService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          QrCodeService qrCodeService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Lists all registered accounts with their current balances.
     *
     * @return List of account summary DTOs
     */
    @Transactional(readOnly = true)
    public List<AccountResponseDto> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapAccountToDto)
                .toList();
    }

    /**
     * Executes an atomic money transfer between two bank accounts.
     * Validates account presence, status, and sufficient funds before committing the debit and credit.
     *
     * @param request Transfer request parameters
     * @return Completed transaction receipt with generated QR code
     * @throws BankingException If any business rule is violated
     */
    @Transactional
    public TransactionResponseDto transferMoney(TransferRequestDto request) {
        if (request.sourceAccountNumber().equalsIgnoreCase(request.destinationAccountNumber())) {
            throw new BankingException("La cuenta de origen y destino no pueden ser iguales.");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("El monto a transferir debe ser mayor a cero.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() -> new BankingException("La cuenta de origen no existe: " + request.sourceAccountNumber()));

        Account destinationAccount = accountRepository.findByAccountNumber(request.destinationAccountNumber())
                .orElseThrow(() -> new BankingException("La cuenta de destino no existe: " + request.destinationAccountNumber()));

        if (!"ACTIVA".equalsIgnoreCase(sourceAccount.getStatus())) {
            throw new BankingException("La cuenta de origen se encuentra bloqueada o inactiva.");
        }

        if (!"ACTIVA".equalsIgnoreCase(destinationAccount.getStatus())) {
            throw new BankingException("La cuenta de destino no está habilitada para recibir transferencias.");
        }

        if (sourceAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new BankingException(String.format(
                    "Fondos insuficientes. Saldo disponible: $%s, monto solicitado: $%s",
                    sourceAccount.getBalance(), request.amount()
            ));
        }

        // Apply ledger movements
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.amount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.amount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Generate unique reference
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String shortId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String reference = "TRX-" + datePrefix + "-" + shortId;

        LocalDateTime now = LocalDateTime.now();

        // Generate QR Code data
        String qrData = qrCodeService.generateQrData(
                reference,
                sourceAccount.getAccountNumber(),
                destinationAccount.getAccountNumber(),
                request.amount().toPlainString(),
                now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(reference);
        transaction.setSourceAccountNumber(sourceAccount.getAccountNumber());
        transaction.setDestinationAccountNumber(destinationAccount.getAccountNumber());
        transaction.setAmount(request.amount());
        transaction.setTimestamp(now);
        transaction.setStatus(TransactionStatus.EXITOSA);
        transaction.setDescription(request.description() != null && !request.description().isBlank()
                ? request.description()
                : "Transferencia entre cuentas");
        transaction.setQrCodeData(qrData);

        Transaction saved = transactionRepository.save(transaction);
        return mapTransactionToDto(saved);
    }

    /**
     * Retrieves the complete transaction history ordered from newest to oldest.
     *
     * @return List of transaction DTOs
     */
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAllByOrderByTimestampDesc().stream()
                .map(this::mapTransactionToDto)
                .toList();
    }

    /**
     * Looks up an individual transaction receipt by its reference code.
     *
     * @param reference Transaction reference
     * @return Transaction DTO receipt
     */
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionByReference(String reference) {
        Transaction transaction = transactionRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new BankingException("No se encontró la transacción con referencia: " + reference));
        return mapTransactionToDto(transaction);
    }

    private AccountResponseDto mapAccountToDto(Account a) {
        return new AccountResponseDto(
                a.getId(),
                a.getAccountNumber(),
                a.getAccountHolder(),
                a.getBalance(),
                a.getAccountType(),
                a.getStatus()
        );
    }

    private TransactionResponseDto mapTransactionToDto(Transaction t) {
        return new TransactionResponseDto(
                t.getId(),
                t.getTransactionReference(),
                t.getSourceAccountNumber(),
                t.getDestinationAccountNumber(),
                t.getAmount(),
                t.getTimestamp(),
                t.getStatus(),
                t.getDescription(),
                t.getQrCodeData()
        );
    }
}

