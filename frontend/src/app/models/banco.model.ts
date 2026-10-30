export interface BankAccount {
  id: number;
  accountNumber: string;
  accountHolder: string;
  balance: number;
  accountType: string;
  status: string;
}

export interface TransferRequest {
  sourceAccountNumber: string;
  destinationAccountNumber: string;
  amount: number;
  description?: string;
}

export interface TransactionReceipt {
  id: number;
  transactionReference: string;
  sourceAccountNumber: string;
  destinationAccountNumber: string;
  amount: number;
  timestamp: string;
  status: 'EXITOSA' | 'RECHAZADA' | 'FALLIDA';
  description: string;
  qrCodeData: string;
}

