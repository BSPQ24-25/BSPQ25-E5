package com.cinema_seat_booking.service;
import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.Payment.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public Payment processPayment(Reservation reservation, String paymentMethod, double amount) {
        Payment payment = new Payment(reservation, paymentMethod, amount);
        boolean paymentSuccessful = simulatePaymentGateway();
        if (paymentSuccessful) 
        {
            payment.setStatus(PaymentStatus.COMPLETED);
            reservation.setReservationState(Reservation.ReservationState.PAYED);
        } 
        else 
        {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return payment;
    }

    private boolean simulatePaymentGateway() {
        return true;
    }
}