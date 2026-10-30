package com.udea.apptdea.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Transfer Object record used for incoming reservation creation requests.
 *
 * @param customerName Full name of the customer
 * @param date         Date of the reservation
 * @param time         Time of the reservation
 * @param service      Name of the requested service
 */
public record ReservationRequestDto(
    @NotBlank(message = "Customer name is mandatory")
    String customerName,

    @NotNull(message = "Reservation date is mandatory")
    LocalDate date,

    @NotNull(message = "Reservation time is mandatory")
    LocalTime time,

    @NotBlank(message = "Service is mandatory")
    String service
) {
}

