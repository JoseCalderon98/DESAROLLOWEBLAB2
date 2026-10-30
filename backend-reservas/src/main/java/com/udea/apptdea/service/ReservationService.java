package com.udea.apptdea.service;

import com.udea.apptdea.dto.ReservationRequestDto;
import com.udea.apptdea.dto.ReservationResponseDto;
import com.udea.apptdea.entity.Reservation;
import com.udea.apptdea.entity.ReservationStatus;
import com.udea.apptdea.exception.BusinessException;
import com.udea.apptdea.exception.ResourceNotFoundException;
import com.udea.apptdea.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer containing the core business rules for reservations.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * Constructs the ReservationService with its required repository dependency.
     *
     * @param reservationRepository Repository for reservation persistence
     */
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Retrieves all reservations registered in the system.
     *
     * @return List of reservation response DTOs
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    /**
     * Creates a new reservation only if no existing active reservation occupies the same date and time slot.
     *
     * @param requestDto The incoming reservation request data
     * @return The saved reservation details as a DTO
     * @throws BusinessException If a reservation already exists for the specified date and time
     */
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        boolean exists = reservationRepository.existsByDateAndTimeAndStatusNot(
                requestDto.date(),
                requestDto.time(),
                ReservationStatus.CANCELADA
        );

        if (exists) {
            throw new BusinessException("A reservation already exists for the specified date and time.");
        }

        Reservation entity = new Reservation();
        entity.setCustomerName(requestDto.customerName());
        entity.setDate(requestDto.date());
        entity.setTime(requestDto.time());
        entity.setService(requestDto.service());
        entity.setStatus(ReservationStatus.ACTIVA);

        Reservation saved = reservationRepository.save(entity);
        return mapToResponseDto(saved);
    }

    /**
     * Cancels an existing reservation by its identifier.
     *
     * @param id The identifier of the reservation to cancel
     * @return The updated reservation details as a DTO
     * @throws ResourceNotFoundException If the reservation with given id does not exist
     */
    @Transactional
    public ReservationResponseDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found."));

        reservation.setStatus(ReservationStatus.CANCELADA);
        Reservation updated = reservationRepository.save(reservation);
        return mapToResponseDto(updated);
    }

    /**
     * Helper method to map a Reservation JPA entity to its Response DTO record.
     *
     * @param reservation The entity to map
     * @return Mapped DTO record
     */
    private ReservationResponseDto mapToResponseDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getService(),
                reservation.getStatus()
        );
    }
}

