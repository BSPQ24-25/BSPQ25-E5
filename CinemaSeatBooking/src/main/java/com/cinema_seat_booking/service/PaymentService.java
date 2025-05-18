/**
 * @file PaymentService.java
 * @brief Service class for managing payment processing in the cinema booking system.
 *
 * @details
 * This class provides methods to process payments for reservations,
 * interacting with {@link PaymentRepository} and {@link ReservationRepository}.
 * It handles updating payment status and reservation state accordingly.
 *
 * @see Payment
 * @see Reservation
 * @see PaymentStatus
 * @see ReservationState
 * 
 * @author 
 * BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.PaymentStatus;
import com.cinema_seat_booking.model.ReservationState;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class PaymentService
 * @brief Service responsible for processing payments related to reservations.
 *
 *        Handles the payment workflow, including updating payment status and
 *        reservation states.
 */
@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * @brief Processes a payment for a given reservation.
     *
     * @param reservation   the {@link Reservation} object for which payment is
     *                      being processed
     * @param paymentMethod the payment method used (e.g., credit card, PayPal)
     * @param amount        the amount to be paid
     * @param date          the date of the payment (as a string)
     * @return the updated {@link Payment} entity after processing
     *
     * @details
     *          This method simulates a payment gateway interaction. If the payment
     *          is successful, it updates the payment status to COMPLETED and the
     *          reservation
     *          state to PAID. Otherwise, the payment status is set to FAILED.
     */
    public Payment processPayment(Reservation reservation, String paymentMethod, double amount, String date) {
        Payment payment = reservation.getPayment();
        boolean paymentSuccessful = simulatePaymentGateway();
        if (paymentSuccessful) {
            if (reservation.getReservationState() == ReservationState.PAID) {
                throw new IllegalStateException("Reservation is already paid.");
            }
            payment.setStatus(PaymentStatus.COMPLETED);
            reservation.setReservationState(ReservationState.PAID);
            reservation.setPayment(payment);
            reservationRepository.save(reservation);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return paymentRepository.save(payment);
    }

    /**
     * @brief Simulates the interaction with a payment gateway.
     *
     * @return always returns true to indicate a successful payment in this
     *         simulation.
     *
     * @details
     *          In a real application, this method would integrate with an external
     *          payment provider.
     */
    public boolean simulatePaymentGateway() {
        return true;
    }
}
