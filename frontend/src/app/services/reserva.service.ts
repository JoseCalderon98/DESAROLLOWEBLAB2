import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';
import { environment } from '../../environments/environment';
import { Reservation, ReservationRequest } from '../models/reserva.model';

/**
 * Service handling HTTP communication with the Apptdea Spring Boot backend using Axios.
 */
@Injectable({
  providedIn: 'root'
})
export class ReservaService {
  private readonly client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: environment.reservasApiUrl,
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  /**
   * Retrieves all registered reservations from the backend.
   */
  async getAllReservations(): Promise<Reservation[]> {
    const response = await this.client.get<Reservation[]>('');
    return response.data;
  }

  /**
   * Sends a request to create a new reservation.
   *
   * @param request Payload containing customer name, date, time, and service
   */
  async createReservation(request: ReservationRequest): Promise<Reservation> {
    const response = await this.client.post<Reservation>('', request);
    return response.data;
  }

  /**
   * Cancels an existing reservation by its ID.
   *
   * @param id The reservation ID
   */
  async cancelReservation(id: number): Promise<Reservation> {
    const response = await this.client.delete<Reservation>(`/${id}`);
    return response.data;
  }
}

