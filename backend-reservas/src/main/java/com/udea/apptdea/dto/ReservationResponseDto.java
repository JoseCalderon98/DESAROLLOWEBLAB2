package com.udea.apptdea.dto;

import com.udea.apptdea.entity.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object record returned to clients with reservation details.
 *
 * @param id           Unique reservation ID
 * @param customerName Customer name
 * @param date         Reservation date
 * @param time         Reservation time
 * @param service      Service name
 * @param status       Reservation status (ACTIVA or CANCELADA)
 */
public record ReservationResponseDto(
    Long id,
    String customerName,
    LocalDate date,
    LocalTime time,
    String service,
    ReservationStatus status
) {
}
