/**
 * @package com.cinema_seat_booking.controller
 * @brief REST controller for handling payment processing.
 *
 * Provides an endpoint to process payments associated with reservations.
 */
package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.service.PaymentService;
import com.cinema_seat_booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @class PaymentController
 * @brief Controller responsible for managing payment operations.
 *
 * This controller exposes REST endpoints related to payment processing
 * for reservations made in the cinema seat booking system.
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    /**
     * Processes a payment for a given reservation.
     *
     * @param reservationId the ID of the reservation to be paid
     * @param paymentMethod the method used for payment (e.g., credit card, PayPal)
     * @param amount the payment amount
     * @param date the date of the payment in string format (e.g., "2024-05-01")
     * @return the processed {@link Payment} object
     */
    @PostMapping("/process")
    public Payment processPayment(
            @RequestParam Long reservationId,
            @RequestParam String paymentMethod,
            @RequestParam double amount,
            @RequestParam String date) {

        Reservation reservation = reservationService.getReservationById(reservationId);
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        return payment;
    }
}
