
package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomTest {

    @Test
    void TestGettersAndSetters() {
        Room room = new Room();
        
        room.setId(1L);
        room.setName("Room A");

        List<Seat> seats = new ArrayList<>();
        Seat seat1 = new Seat(1, room);
        Seat seat2 = new Seat(2, room);
        seats.add(seat1);
        seats.add(seat2);

        room.setSeats(seats);

        assertEquals(1L, room.getId());
        assertEquals("Room A", room.getName());
        assertEquals(seats, room.getSeats());
    }

    @Test
    void TestSeatCounts() {
        Room room = new Room("Room B", new ArrayList<>());

        Seat seat1 = new Seat(1, room);
        Seat seat2 = new Seat(2, room);
        Seat seat3 = new Seat(3, room);

        seat1.setReserved(false);
        seat2.setReserved(true);
        seat3.setReserved(false);

        room.getSeats().add(seat1);
        room.getSeats().add(seat2);
        room.getSeats().add(seat3);
        //quite the mistake here i need to come back ... 
        assertEquals(3, room.getSeatCount());
        assertEquals(2, room.getAvailableSeats());
        assertEquals(1, room.getReservedSeats());
    }
}
 
