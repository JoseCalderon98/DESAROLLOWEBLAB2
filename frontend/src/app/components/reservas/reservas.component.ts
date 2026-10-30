import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReservaService } from '../../services/reserva.service';
import { Reservation, ReservationRequest } from '../../models/reserva.model';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservas.component.html',
  styleUrl: './reservas.component.css'
})
export class ReservasComponent implements OnInit {
  reservations: Reservation[] = [];
  loading = false;

  // Form model
  formData: ReservationRequest = {
    customerName: '',
    date: '',
    time: '',
    service: ''
  };

  // Pre-configured services list
  availableServices: string[] = [
    'Capacitación',
    'Asesoría Técnica Especializada',
    'Auditoría de Software',
    'Consultoría en Arquitectura Cloud',
    'Soporte y Mantenimiento'
  ];

  // Toast notifications
  toastMessage: string | null = null;
  toastType: 'success' | 'error' = 'success';

  constructor(private readonly reservaService: ReservaService) {}

  async ngOnInit(): Promise<void> {
    await this.loadReservations();
  }

  async loadReservations(): Promise<void> {
    this.loading = true;
    try {
      this.reservations = await this.reservaService.getAllReservations();
    } catch (error: any) {
      this.showToast('No fue posible cargar las reservas del servidor.', 'error');
    } finally {
      this.loading = false;
    }
  }

  async onSubmit(): Promise<void> {
    if (!this.formData.customerName.trim() || !this.formData.date || !this.formData.time || !this.formData.service) {
      this.showToast('Todos los campos son obligatorios.', 'error');
      return;
    }

    try {
      // Normalize time to HH:mm:ss if needed
      let formattedTime = this.formData.time;
      if (formattedTime.length === 5) {
        formattedTime += ':00';
      }

      await this.reservaService.createReservation({
        customerName: this.formData.customerName.trim(),
        date: this.formData.date,
        time: formattedTime,
        service: this.formData.service
      });

      this.showToast('Reserva creada exitosamente.', 'success');
      this.resetForm();
      await this.loadReservations();
    } catch (error: any) {
      const serverMessage = error.response?.data?.message || 'Error al guardar la reserva.';
      this.showToast(serverMessage, 'error');
    }
  }

  async cancelReservation(id: number): Promise<void> {
    if (!confirm('¿Está seguro de que desea cancelar esta reserva?')) {
      return;
    }

    try {
      await this.reservaService.cancelReservation(id);
      this.showToast('Reserva cancelada correctamente.', 'success');
      await this.loadReservations();
    } catch (error: any) {
      const serverMessage = error.response?.data?.message || 'Error al cancelar la reserva.';
      this.showToast(serverMessage, 'error');
    }
  }

  resetForm(): void {
    this.formData = {
      customerName: '',
      date: '',
      time: '',
      service: ''
    };
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
