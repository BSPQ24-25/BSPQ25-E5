package com.cinema_seat_booking.CinemaSeatBooking.unit;
import com.cinema_seat_booking.model.*;


import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
public class SeatTest {

    @Test
    void TestGettersAndSetters(){
        Seat seat =new Seat();
        Room room = new Room();
        seat.setSeatNumber(1);
        seat.setReserved(true);
        seat.setRoom(room);
        seat.setId((long)100);

        assertEquals(room,seat.getRoom() );
        assertEquals(1,seat.getSeatNumber() );
        assertEquals(true,seat.isReserved());
        assertEquals(100,seat.getId() );

    }
    
}
