package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ReservationDTO;
import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.service.ReservationService;
import com.cinema_seat_booking.service.ScreeningService;
import com.cinema_seat_booking.service.SeatService;
import com.cinema_seat_booking.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScreeningService screeningService;

    @Autowired
    private SeatService seatService;

    // Crear una reserva
    @PostMapping
public ResponseEntity<?> createReservation(
        @RequestParam String username,
        @RequestParam Long screeningId,
        @RequestParam Long seatId) {
    try {
        User user = userService.getUserByUsername(username);
        Screening screening = screeningService.getScreeningById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + screeningId));
        Seat seat = seatService.getSeatById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + seatId));

        Reservation reservation = reservationService.createReservation(user, screening, seat, "NONE");
        return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        log.warn("Invalid request: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        log.error("Unexpected error creating reservation", e);
        return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    // Cancelar una reserva
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error canceling reservation", e);
            return new ResponseEntity<>("Error canceling reservation: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Consultar una reserva por ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservation(@PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving reservation", e);
            return new ResponseEntity<>("Reservation not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
