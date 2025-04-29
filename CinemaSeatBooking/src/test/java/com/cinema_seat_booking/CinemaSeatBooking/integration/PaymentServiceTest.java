package com.cinema_seat_booking.CinemaSeatBooking.integration;
import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.PaymentService;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.PaymentStatus;
import com.cinema_seat_booking.model.ReservationState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    private PaymentService paymentService;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
        reservation = new Reservation();
    }

    @Test
    void testProcessPayment_Successful() {
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";

        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);

        assertNotNull(payment);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertEquals(payment, reservation.getPayment());
        assertEquals(amount, payment.getAmount());
        assertEquals(date, payment.getPaymentDate());
        assertEquals(paymentMethod, payment.getPaymentMethod());
    }
}
