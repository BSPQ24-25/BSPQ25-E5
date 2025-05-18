
package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.model.Seat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomTest {

    @Test
    public void testDefaultConstructor() {
        Room room = new Room();
        assertNotNull(room.getSeats());
        assertNotNull(room.getScreenings());
        assertEquals(0, room.getSeats().size());
        assertEquals(0, room.getScreenings().size());
    }

    @Test
    public void testConstructorWithNameAndSeats() {
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat(1, false, null));
        seats.add(new Seat(2, true, null));

        Room room = new Room("Room A", seats);

        assertEquals("Room A", room.getName());
        assertEquals(2, room.getSeatCount());

        for (Seat seat : room.getSeats()) {
            assertEquals(room, seat.getRoom());
        }
    }

    @Test
    public void testConstructorWithNameOnlyCreates20Seats() {
        Room room = new Room("Room B");

        assertEquals("Room B", room.getName());
        assertEquals(20, room.getSeatCount());

        for (int i = 0; i < 20; i++) {
            Seat seat = room.getSeats().get(i);
            assertEquals(i + 1, seat.getSeatNumber());
            assertFalse(seat.isReserved());
            assertEquals(room, seat.getRoom());
        }
    }

    @Test
    public void testAddSeatAddsAndSetsRoom() {
        Room room = new Room("Room C");
        Seat seat = new Seat(1, false, null);

        room.addSeat(seat);

        assertTrue(room.getSeats().contains(seat));
        assertEquals(room, seat.getRoom());
    }

    @Test
    public void testAddScreeningAddsAndSetsRoom() {
        Room room = new Room("Room D");
        Screening screening = new Screening();

        room.addScreening(screening);

        assertTrue(room.getScreenings().contains(screening));
        assertEquals(room, screening.getRoom());
    }

    @Test
    public void testSeatAvailabilityCounts() {
        Room room = new Room("Room E");

        Seat available = new Seat(1, false, room);
        Seat reserved = new Seat(2, true, room);
        room.setSeats(List.of(available, reserved));

        assertEquals(2, room.getSeatCount());
        assertEquals(1, room.getAvailableSeats());
        assertEquals(1, room.getReservedSeats());
    }

    @Test
    public void testSettersAndGetters() {
        Room room = new Room();

        room.setId(100L);
        assertEquals(100L, room.getId());

        room.setName("Test Room");
        assertEquals("Test Room", room.getName());

        List<Seat> seats = new ArrayList<>();
        room.setSeats(seats);
        assertEquals(seats, room.getSeats());

        List<Screening> screenings = new ArrayList<>();
        room.setScreenings(screenings);
        assertEquals(screenings, room.getScreenings());
    }
}
