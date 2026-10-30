import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';
import { environment } from '../../environments/environment';
import { BankAccount, TransactionReceipt, TransferRequest } from '../models/banco.model';

/**
 * Service managing financial money transfers, account balances, and transaction history.
 */
@Injectable({
  providedIn: 'root'
})
export class BancoService {
  private readonly client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: environment.bancoApiUrl,
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  /**
   * Retrieves all bank accounts with current balances.
   */
  async getAccounts(): Promise<BankAccount[]> {
    const response = await this.client.get<BankAccount[]>('/cuentas');
    return response.data;
  }

  /**
   * Dispatches a money transfer between two accounts and receives a transaction receipt with QR.
   */
  async transferMoney(request: TransferRequest): Promise<TransactionReceipt> {
    const response = await this.client.post<TransactionReceipt>('/transferencias', request);
    return response.data;
  }

  /**
   * Retrieves the full ledger history of banking transactions.
   */
  async getTransactions(): Promise<TransactionReceipt[]> {
    const response = await this.client.get<TransactionReceipt[]>('/transacciones');
    return response.data;
  }

  /**
   * Looks up a specific transaction receipt by reference.
   */
  async getTransactionByReference(reference: string): Promise<TransactionReceipt> {
    const response = await this.client.get<TransactionReceipt>(`/transacciones/${reference}`);
    return response.data;
  }
}

