import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { BancoService } from '../../services/banco.service';
import { BankAccount, TransactionReceipt, TransferRequest } from '../../models/banco.model';

@Component({
  selector: 'app-banco',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './banco.component.html',
  styleUrl: './banco.component.css'
})
export class BancoComponent implements OnInit {
  accounts: BankAccount[] = [];
  transactions: TransactionReceipt[] = [];
  loading = false;
  submitting = false;

  // Transfer form model
  transferData: TransferRequest = {
    sourceAccountNumber: '',
    destinationAccountNumber: '',
    amount: 100000,
    description: ''
  };

  // Receipt Modal state
  selectedReceipt: TransactionReceipt | null = null;
  safeQrSvg: SafeHtml | null = null;

  // Toast notifications
  toastMessage: string | null = null;
  toastType: 'success' | 'error' = 'success';

  constructor(
    private readonly bancoService: BancoService,
    private readonly sanitizer: DomSanitizer
  ) {}

  async ngOnInit(): Promise<void> {
    await this.refreshAll();
  }

  async refreshAll(): Promise<void> {
    this.loading = true;
    try {
      await Promise.all([this.loadAccounts(), this.loadTransactions()]);
    } finally {
      this.loading = false;
    }
  }

  async loadAccounts(): Promise<void> {
    try {
      this.accounts = await this.bancoService.getAccounts();
      if (this.accounts.length >= 2 && !this.transferData.sourceAccountNumber) {
        this.transferData.sourceAccountNumber = this.accounts[0].accountNumber;
        this.transferData.destinationAccountNumber = this.accounts[1].accountNumber;
      }
    } catch (err) {
      this.showToast('No fue posible cargar las cuentas bancarias.', 'error');
    }
  }

  async loadTransactions(): Promise<void> {
    try {
      this.transactions = await this.bancoService.getTransactions();
    } catch (err) {
      this.showToast('No fue posible cargar el histórico de transferencias.', 'error');
    }
  }

  async executeTransfer(): Promise<void> {
    if (!this.transferData.sourceAccountNumber || !this.transferData.destinationAccountNumber) {
      this.showToast('Por favor seleccione cuenta de origen y destino.', 'error');
      return;
    }

    if (this.transferData.sourceAccountNumber === this.transferData.destinationAccountNumber) {
      this.showToast('La cuenta origen y destino deben ser diferentes.', 'error');
      return;
    }

    if (!this.transferData.amount || this.transferData.amount <= 0) {
      this.showToast('El monto a transferir debe ser mayor a 0.', 'error');
      return;
    }

    this.submitting = true;
    try {
      const receipt = await this.bancoService.transferMoney({
        sourceAccountNumber: this.transferData.sourceAccountNumber,
        destinationAccountNumber: this.transferData.destinationAccountNumber,
        amount: Number(this.transferData.amount),
        description: this.transferData.description || 'Transferencia entre cuentas'
      });

      this.showToast('¡Transferencia procesada con éxito!', 'success');
      this.openReceiptModal(receipt);
      await this.refreshAll();
    } catch (error: any) {
      const serverMsg = error.response?.data?.message || 'Error al ejecutar la transferencia.';
      this.showToast(serverMsg, 'error');
    } finally {
      this.submitting = false;
    }
  }

  openReceiptModal(receipt: TransactionReceipt): void {
    this.selectedReceipt = receipt;
    if (receipt.qrCodeData) {
      this.safeQrSvg = this.sanitizer.bypassSecurityTrustHtml(receipt.qrCodeData);
    } else {
      this.safeQrSvg = null;
    }
  }

  closeReceiptModal(): void {
    this.selectedReceipt = null;
    this.safeQrSvg = null;
  }

  getSourceAccountBalance(): number | null {
    const acc = this.accounts.find(a => a.accountNumber === this.transferData.sourceAccountNumber);
    return acc ? acc.balance : null;
  }

  showToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => {
      if (this.toastMessage === message) {
        this.toastMessage = null;
      }
    }, 5000);
  }
}

