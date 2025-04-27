package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ReservationDTO;
import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.service.ReservationService;
import com.cinema_seat_booking.service.ScreeningService;
import com.cinema_seat_booking.service.SeatService;
import com.cinema_seat_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // Crear una reserva
    @PostMapping
    public ReservationDTO createReservation(
            @RequestParam String username,
            @RequestParam Long screeningId,
            @RequestParam Long seatId) {
        // Buscar entidades
        User user = userService.getUserByUsername(username);
        Screening screening = screeningService.getScreeningById(screeningId)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + screeningId));
        Seat seat = seatService.getSeatById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + seatId));

        // Crear la reserva
        Reservation reservation = reservationService.createReservation(user, screening, seat, "NONE");
        return new ReservationDTO(reservation);
    }

    // Cancelar una reserva
    @DeleteMapping("/{reservationId}")
    public void cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
    }

    // Consultar una reserva por ID
    @GetMapping("/{reservationId}")
    public ReservationDTO getReservation(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        return new ReservationDTO(reservation);
    }

}
