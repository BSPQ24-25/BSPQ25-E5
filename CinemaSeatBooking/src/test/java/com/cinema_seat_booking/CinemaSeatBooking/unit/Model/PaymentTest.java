package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    public void testDefaultConstructor() {
        Payment payment = new Payment();
        assertNull(payment.getId());
        assertNull(payment.getPaymentMethod());
        assertNull(payment.getPaymentDate());
        assertEquals(0.0, payment.getAmount());
        assertNull(payment.getStatus());
        assertNull(payment.getReservation());
    }

    @Test
    public void testConstructorWithMethodAmountDateStatus() {
        Payment payment = new Payment("Credit Card", 100.0, "2025-05-19", PaymentStatus.COMPLETED);

        assertEquals("Credit Card", payment.getPaymentMethod());
        assertEquals(100.0, payment.getAmount());
        assertEquals("2025-05-19", payment.getPaymentDate());
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void testConstructorWithMethodAmountDate() {
        Payment payment = new Payment("PayPal", 75.5, "2025-05-20");

        assertEquals("PayPal", payment.getPaymentMethod());
        assertEquals(75.5, payment.getAmount());
        assertEquals("2025-05-20", payment.getPaymentDate());
        assertNull(payment.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        Payment payment = new Payment();

        payment.setId(5L);
        assertEquals(5L, payment.getId());

        payment.setPaymentMethod("Bizum");
        assertEquals("Bizum", payment.getPaymentMethod());

        payment.setAmount(42.42);
        assertEquals(42.42, payment.getAmount());

        payment.setPaymentDate("2025-06-01");
        assertEquals("2025-06-01", payment.getPaymentDate());

        payment.setStatus(PaymentStatus.PENDING);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        Reservation reservation = new Reservation();
        payment.setReservation(reservation);
        assertEquals(reservation, payment.getReservation());
    }
}
