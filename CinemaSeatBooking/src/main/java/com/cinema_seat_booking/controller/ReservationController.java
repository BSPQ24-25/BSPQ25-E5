/**
 * @package com.cinema_seat_booking.controller
 * @brief REST controller for managing seat reservations.
 *
 * Provides endpoints to create, retrieve, and cancel reservations
 * involving users, screenings, and seats.
 */
package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ReservationDTO;
import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.service.ReservationService;
import com.cinema_seat_booking.service.ScreeningService;
import com.cinema_seat_booking.service.SeatService;
import com.cinema_seat_booking.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * @class ReservationController
 * @brief Exposes REST endpoints for reservation management.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private SeatService seatService;

    /**
     * Creates a new reservation for a user, screening, and seat.
     *
     * @param username    the username of the user making the reservation
     * @param screeningId the ID of the screening
     * @param seatId      the ID of the seat to reserve
     * @return the created {@link ReservationDTO}
     */
    @Operation(summary = "Create a reservation", description = "Creates a reservation for a specific user, screening, and seat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or seat unavailable")
    })
    @PostMapping
    public ReservationDTO createReservation(
            @RequestParam @Parameter(description = "Username of the user") String username,
            @RequestParam @Parameter(description = "ID of the screening") Long screeningId,
            @RequestParam @Parameter(description = "ID of the seat") Long seatId) {

        User user = userService.getUserByUsername(username);
        Screening screening = screeningService.getScreeningById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + screeningId));
        Seat seat = seatService.getSeatById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + seatId));

        Reservation reservation = reservationService.createReservation(user, screening, seat, "NONE");
        return new ReservationDTO(reservation);
    }

    /**
     * Retrieves all reservations in the system.
     *
     * @return a list of {@link ReservationDTO}
     */
    @Operation(summary = "Get all reservations", description = "Returns all reservations in the system.")
    @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully")
    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return reservations.stream()
                .map(ReservationDTO::new)
                .toList();
    }

    /**
     * Retrieves all reservations for a given user.
     *
     * @param username the username of the user
     * @return a list of {@link ReservationDTO} for that user
     */
    @Operation(summary = "Get reservations of a user", description = "Returns all reservations for a specific user.")
    @ApiResponse(responseCode = "200", description = "User's reservations retrieved successfully")
    @GetMapping("/user/{username}")
    public List<ReservationDTO> getAllReservationsOfUser(
            @PathVariable @Parameter(description = "Username of the user") String username) {
        List<Reservation> reservations = reservationService.getAllReservationsOfUser(username);
        return reservations.stream()
                .map(ReservationDTO::new)
                .toList();
    }

    /**
     * Cancels an existing reservation by ID.
     *
     * @param reservationId the ID of the reservation to cancel
     */
    @Operation(summary = "Cancel reservation", description = "Cancels an existing reservation by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @DeleteMapping("/{reservationId}")
    public void cancelReservation(
            @PathVariable @Parameter(description = "ID of the reservation to cancel") Long reservationId) {
        reservationService.cancelReservation(reservationId);
    }

    /**
     * Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation
     * @return the {@link ReservationDTO} if found
     */
    @Operation(summary = "Get reservation by ID", description = "Fetches a reservation's details by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @GetMapping("/{reservationId}")
    public ReservationDTO getReservation(
            @PathVariable @Parameter(description = "ID of the reservation") Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        return new ReservationDTO(reservation);
    }

}
