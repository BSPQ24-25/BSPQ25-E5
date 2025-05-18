package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class SeatTest {

    @Test
    void testDefaultConstructor() {
        Seat seat = new Seat();
        assertNotNull(seat);
    }

    @Test
    void testConstructorWithAllFields() {
        Room room = new Room("Room A");
        Seat seat = new Seat(12, true, room);

        assertEquals(12, seat.getSeatNumber());
        assertTrue(seat.isReserved());
        assertEquals(room, seat.getRoom());
    }

    @Test
    void testConstructorWithoutReservedDefaultsToFalse() {
        Room room = new Room("Room B");
        Seat seat = new Seat(8, room);

        assertEquals(8, seat.getSeatNumber());
        assertFalse(seat.isReserved());
        assertEquals(room, seat.getRoom());
    }

    @Test
    void testSettersAndGetters() {
        Seat seat = new Seat();
        Room room = new Room("Room C");
        Reservation reservation = new Reservation();

        seat.setId(1L);
        seat.setSeatNumber(5);
        seat.setReserved(true);
        seat.setRoom(room);
        seat.setReservation(reservation);

        assertEquals(1L, seat.getId());
        assertEquals(5, seat.getSeatNumber());
        assertTrue(seat.isReserved());
        assertEquals(room, seat.getRoom());
        assertEquals(reservation, seat.getReservation());
    }

}
