package com.udea.banktdea.controller;

import com.udea.banktdea.dto.AccountResponseDto;
import com.udea.banktdea.dto.TransactionResponseDto;
import com.udea.banktdea.dto.TransferRequestDto;
import com.udea.banktdea.service.BankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing financial transfer and transaction audit endpoints.
 */
@RestController
@RequestMapping("/api/banco")
@Tag(name = "Sistema Bancario TdeA", description = "Endpoints para transferencias entre cuentas, histórico y código QR")
public class BankingController {

    private final BankingService bankingService;

    /**
     * Injects the BankingService via constructor.
     *
     * @param bankingService Business banking service
     */
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    /**
     * Lists all registered bank accounts and their active balances.
     *
     * @return List of accounts with HTTP 200 OK
     */
    @GetMapping("/cuentas")
    @Operation(summary = "Listar cuentas", description = "Obtiene la lista de todas las cuentas bancarias registradas")
    @ApiResponse(responseCode = "200", description = "Cuentas obtenidas exitosamente")
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccounts());
    }

    /**
     * Executes a funds transfer between two accounts and produces a digital receipt with QR code.
     *
     * @param request Transfer request containing source, destination, amount and note
     * @return Completed transaction receipt with HTTP 201 Created
     */
    @PostMapping("/transferencias")
    @Operation(summary = "Realizar transferencia", description = "Transfiere fondos entre cuentas y genera código QR")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transferencia realizada con éxito"),
            @ApiResponse(responseCode = "400", description = "Error de validación o fondos insuficientes")
    })
    public ResponseEntity<TransactionResponseDto> transferMoney(@Valid @RequestBody TransferRequestDto request) {
        TransactionResponseDto receipt = bankingService.transferMoney(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt);
    }

    /**
     * Retrieves the chronological transaction history of all account transfers.
     *
     * @return List of transactions with HTTP 200 OK
     */
    @GetMapping("/transacciones")
    @Operation(summary = "Histórico de transacciones", description = "Obtiene todas las transacciones ordenadas descendentemente")
    @ApiResponse(responseCode = "200", description = "Histórico obtenido exitosamente")
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {
        return ResponseEntity.ok(bankingService.getAllTransactions());
    }

    /**
     * Retrieves an individual transaction receipt by its reference code.
     *
     * @param reference Transaction reference string
     * @return Transaction receipt with QR code
     */
    @GetMapping("/transacciones/{reference}")
    @Operation(summary = "Consultar transacción por referencia", description = "Busca el detalle de una transacción y su código QR")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transacción encontrada"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<TransactionResponseDto> getTransactionByReference(@PathVariable String reference) {
        return ResponseEntity.ok(bankingService.getTransactionByReference(reference));
    }
}

