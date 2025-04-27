package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

  
    @Test
    void testConstructor() {
        String method = "Debit Card";
        double amount = 150.0;
        String date = "2025-04-21";

        Payment payment = new Payment(method, amount, date, PaymentStatus.PENDING);

        assertEquals(method, payment.getPaymentMethod());
        assertEquals(amount, payment.getAmount());
        assertEquals(date, payment.getPaymentDate());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Payment payment = new Payment();

        payment.setId(1L);
        payment.setPaymentMethod("Cash");
        payment.setAmount(200.0);
        payment.setPaymentDate("2025-04-22");
        payment.setStatus(PaymentStatus.COMPLETED);

        assertEquals(1L, payment.getId());
        assertEquals("Cash", payment.getPaymentMethod());
        assertEquals(200.0, payment.getAmount());
        assertEquals("2025-04-22", payment.getPaymentDate());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }
}
