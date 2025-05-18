package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import org.junit.jupiter.api.Test;
import com.cinema_seat_booking.model.*;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    public void testDefaultConstructorInitializesPendingState() {
        Reservation reservation = new Reservation();
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
        assertNull(reservation.getUser());
        assertNull(reservation.getScreening());
        assertNull(reservation.getSeat());
        assertNull(reservation.getPayment());
    }

    @Test
    public void testConstructorWithAllFields() {
        User user = new User();
        Screening screening = new Screening();
        Seat seat = new Seat();
        Payment payment = new Payment();

        Reservation reservation = new Reservation(user, screening, payment, seat);

        assertEquals(user, reservation.getUser());
        assertEquals(screening, reservation.getScreening());
        assertEquals(seat, reservation.getSeat());
        assertEquals(payment, reservation.getPayment());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());

        // Check bidirectional relationships
        assertTrue(seat.isReserved());
        assertEquals(reservation, seat.getReservation());
        assertEquals(reservation, payment.getReservation());
    }

    @Test
    public void testConstructorWithoutPaymentCreatesNewPayment() {
        User user = new User();
        Screening screening = new Screening();
        Seat seat = new Seat();

        Reservation reservation = new Reservation(user, screening, seat);

        assertEquals(user, reservation.getUser());
        assertEquals(screening, reservation.getScreening());
        assertEquals(seat, reservation.getSeat());
        assertNotNull(reservation.getPayment());
        assertEquals(reservation, reservation.getPayment().getReservation());
        assertTrue(seat.isReserved());
        assertEquals(reservation, seat.getReservation());
    }

    @Test
    public void testSettersAndGetters() {
        Reservation reservation = new Reservation();

        reservation.setId(10L);
        assertEquals(10L, reservation.getId());

        User user = new User();
        reservation.setUser(user);
        assertEquals(user, reservation.getUser());

        Screening screening = new Screening();
        reservation.setScreening(screening);
        assertEquals(screening, reservation.getScreening());

        Seat seat = new Seat();
        reservation.setSeat(seat);
        assertEquals(seat, reservation.getSeat());
        assertTrue(seat.isReserved());
        assertEquals(reservation, seat.getReservation());

        Payment payment = new Payment();
        reservation.setPayment(payment);
        assertEquals(payment, reservation.getPayment());
        assertEquals(reservation, payment.getReservation());

        reservation.setReservationState(ReservationState.PAID);
        assertEquals(ReservationState.PAID, reservation.getReservationState());
    }
}
