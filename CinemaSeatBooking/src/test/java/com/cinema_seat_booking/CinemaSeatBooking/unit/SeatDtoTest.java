package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.dto.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatDTOTest {

    @Test
    void testSeatDTOConstructorWithSeat() {
        // Arrange
        Room room = new Room("Room1");
        Seat seat = new Seat(1, room);
        seat.setId(1L);
        seat.setReserved(true);

        // Act
        SeatDTO seatDTO = new SeatDTO(seat);

        // Assert
        assertEquals(seat.getId(), seatDTO.getId());
        assertEquals(seat.getSeatNumber(), seatDTO.getSeatNumber());
        assertEquals(seat.isReserved(), seatDTO.isReserved());
        assertEquals(seat.getRoom().getName(), seatDTO.getRoomName());
    }

    @Test
    void testSeatDTOConstructorWithFields() {
        // Act
        SeatDTO seatDTO = new SeatDTO(1L, 1, true, "Room1");

        // Assert
        assertEquals(1L, seatDTO.getId());
        assertEquals(1, seatDTO.getSeatNumber());
        assertTrue(seatDTO.isReserved());
        assertEquals("Room1", seatDTO.getRoomName());
    }

    @Test
    void testSeatDTOSettersAndGetters() {
        // Arrange
        SeatDTO seatDTO = new SeatDTO();

        // Act
        seatDTO.setId(1L);
        seatDTO.setSeatNumber(1);
        seatDTO.setReserved(true);
        seatDTO.setRoomName("Room1");

        // Assert
        assertEquals(1L, seatDTO.getId());
        assertEquals(1, seatDTO.getSeatNumber());
        assertTrue(seatDTO.isReserved());
        assertEquals("Room1", seatDTO.getRoomName());
    }
}