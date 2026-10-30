package com.udea.apptdea.repository;

import com.udea.apptdea.entity.Reservation;
import com.udea.apptdea.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Spring Data JPA Repository for managing {@link Reservation} persistence.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Checks whether a reservation already exists at the specified date and time.
     *
     * @param date The reservation date
     * @param time The reservation time
     * @return {@code true} if a reservation exists for that date and time, {@code false} otherwise
     */
    boolean existsByDateAndTime(LocalDate date, LocalTime time);

    /**
     * Checks whether an active reservation already exists at the specified date and time,
     * ignoring reservations with a specified status (e.g. CANCELADA).
     *
     * @param date   The reservation date
     * @param time   The reservation time
     * @param status The status to exclude
     * @return {@code true} if an active reservation exists, {@code false} otherwise
     */
    boolean existsByDateAndTimeAndStatusNot(LocalDate date, LocalTime time, ReservationStatus status);
}

