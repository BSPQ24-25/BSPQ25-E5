package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.service.PaymentService;
import com.cinema_seat_booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/process")
    public Payment processPayment(@RequestParam Long reservationId, @RequestParam String paymentMethod,
            @RequestParam double amount, @RequestParam String date) {
        Reservation reservation = reservationService.getReservationById(reservationId);
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        return payment;
    }
}