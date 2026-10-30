export type ReservationStatus = 'ACTIVA' | 'CANCELADA';

export interface Reservation {
  id: number;
  customerName: string;
  date: string;
  time: string;
  service: string;
  status: ReservationStatus;
}

export interface ReservationRequest {
  customerName: string;
  date: string;
  time: string;
  service: string;
}

