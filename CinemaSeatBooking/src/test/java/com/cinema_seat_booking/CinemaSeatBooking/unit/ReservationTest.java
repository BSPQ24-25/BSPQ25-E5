package com.cinema_seat_booking.CinemaSeatBooking.unit;


import org.junit.jupiter.api.Test;
import com.cinema_seat_booking.model.*;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testReservationConstructorWithPayment() {
        User user = new User();
        Screening screening = new Screening();
        Payment payment = new Payment();
        Seat seat = new Seat();

        Reservation reservation = new Reservation(user, screening, payment, seat);
        

        assertEquals(user, reservation.getUser());
        assertEquals(screening, reservation.getScreening());
        assertEquals(payment, reservation.getPayment());
        assertEquals(seat, reservation.getSeat());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
    }

    @Test
    void testReservationConstructorWithoutPayment() {
        User user = new User();
        Screening screening = new Screening();
        Seat seat = new Seat();

        Reservation reservation = new Reservation(user, screening, seat);

        assertEquals(user, reservation.getUser());
        assertEquals(screening, reservation.getScreening());
        assertNotNull(reservation.getPayment(), "Payment should be initialized");
        assertEquals(seat, reservation.getSeat());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        Screening screening = new Screening();
        Payment payment = new Payment();
        Seat seat = new Seat();

        Reservation reservation = new Reservation();

        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setScreening(screening);
        reservation.setSeat(seat);
        reservation.setPayment(payment);
        reservation.setReservationState(ReservationState.PENDING);

        assertEquals(1L, reservation.getId());
        assertEquals(user, reservation.getUser());
        assertEquals(screening, reservation.getScreening());
        assertEquals(seat, reservation.getSeat());
        assertEquals(payment, reservation.getPayment());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
    }
}
