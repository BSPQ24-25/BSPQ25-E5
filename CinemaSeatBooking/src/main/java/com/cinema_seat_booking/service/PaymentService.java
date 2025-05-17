package com.cinema_seat_booking.service;
import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.PaymentStatus;
import com.cinema_seat_booking.model.ReservationState;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class PaymentService {

    public Payment processPayment(Reservation reservation, String paymentMethod, double amount, String date) {
        Payment payment = new Payment(paymentMethod, amount, date);
        boolean paymentSuccessful = simulatePaymentGateway();
        if (paymentSuccessful) 
        {

            payment.setStatus(PaymentStatus.COMPLETED);
            reservation.setReservationState(ReservationState.PAID);
            reservation.setPayment(payment);
        } 
        else 
        {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return payment;
    }

    public boolean simulatePaymentGateway() {
        return true;
    }
}