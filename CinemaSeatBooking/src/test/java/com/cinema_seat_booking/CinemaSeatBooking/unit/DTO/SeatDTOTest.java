package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.SeatDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatDTOTest {

    private Seat seat;
    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Room A");

        seat = new Seat();
        seat.setId(10L);
        seat.setSeatNumber(5);
        seat.setReserved(true);
        seat.setRoom(room);
    }

    @Test
    void testConstructorWithEntity() {
        SeatDTO dto = new SeatDTO(seat);

        assertEquals(seat.getId(), dto.getId());
        assertEquals(seat.getSeatNumber(), dto.getSeatNumber());
        assertEquals(seat.isReserved(), dto.isReserved());
        assertEquals(seat.getRoom().getName(), dto.getRoomName());
    }

    @Test
    void testConstructorWithParams() {
        SeatDTO dto = new SeatDTO(20L, 15, false, "Room B");

        assertEquals(20L, dto.getId());
        assertEquals(15, dto.getSeatNumber());
        assertFalse(dto.isReserved());
        assertEquals("Room B", dto.getRoomName());
    }

    @Test
    void testDefaultConstructorAndSetters() {
        SeatDTO dto = new SeatDTO();

        dto.setId(30L);
        dto.setSeatNumber(25);
        dto.setReserved(true);
        dto.setRoomName("Room C");

        assertEquals(30L, dto.getId());
        assertEquals(25, dto.getSeatNumber());
        assertTrue(dto.isReserved());
        assertEquals("Room C", dto.getRoomName());
    }

    @Test
    void testToString() {
        SeatDTO dto = new SeatDTO(40L, 8, true, "Room D");
        String str = dto.toString();

        assertTrue(str.contains("id=40"));
        assertTrue(str.contains("seatNumber=8"));
        assertTrue(str.contains("isReserved=true"));
        assertTrue(str.contains("roomName=Room D"));
    }
}
