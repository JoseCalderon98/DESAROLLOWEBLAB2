package com.udea.apptdea.controller;

import com.udea.apptdea.dto.ReservationRequestDto;
import com.udea.apptdea.dto.ReservationResponseDto;
import com.udea.apptdea.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing endpoints for managing bookings and reservations.
 */
@RestController
@RequestMapping("/reservas")
@Tag(name = "Reservas", description = "Endpoints for reservation management")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Injects the ReservationService via constructor.
     *
     * @param reservationService Business service for reservations
     */
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Lists all reservations currently stored in the system.
     *
     * @return List of reservation records with HTTP 200 OK
     */
    @GetMapping
    @Operation(summary = "List all reservations", description = "Retrieves the complete list of reservations")
    @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Creates a new reservation after validating business constraints.
     *
     * @param requestDto The reservation payload
     * @return The created reservation record with HTTP 201 Created
     */
    @PostMapping
    @Operation(summary = "Create a reservation", description = "Schedules a new reservation slot")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in request body"),
            @ApiResponse(responseCode = "409", description = "Slot already booked for this date and time")
    })
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto created = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Cancels an existing reservation by changing its status to CANCELADA.
     *
     * @param id Identifier of the reservation to cancel
     * @return The updated reservation record with HTTP 200 OK
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a reservation", description = "Marks an existing reservation as cancelled")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    public ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable Long id) {
        ReservationResponseDto cancelled = reservationService.cancelReservation(id);
        return ResponseEntity.ok(cancelled);
    }
}
