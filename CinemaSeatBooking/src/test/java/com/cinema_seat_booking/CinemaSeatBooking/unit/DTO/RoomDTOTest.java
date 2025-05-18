package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.dto.SeatDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RoomDTOTest {

    private Room room;
    private List<Seat> seats;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Test Room");

        seats = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Seat seat = new Seat();
            seat.setId((long) i);
            seat.setSeatNumber(i);
            seat.setRoom(room);
            seats.add(seat);
        }

        room.setSeats(seats);
    }

    @Test
    void testConstructorWithRoomEntity() {
        RoomDTO dto = new RoomDTO(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertNotNull(dto.getSeats());
        assertEquals(3, dto.getSeats().size());

        for (int i = 0; i < seats.size(); i++) {
            assertEquals(seats.get(i).getId(), dto.getSeats().get(i).getId());
        }
    }

    @Test
    void testConstructorWithParams() {
        RoomDTO dto = new RoomDTO(2L, "Param Room", seats);

        assertEquals(2L, dto.getId());
        assertEquals("Param Room", dto.getName());
        assertNotNull(dto.getSeats());
        assertEquals(3, dto.getSeats().size());
    }

    @Test
    void testSettersAndGetters() {
        RoomDTO dto = new RoomDTO(room);
        dto.setId(10L);
        dto.setName("Updated Room");

        List<SeatDTO> newSeatDTOs = Collections.singletonList(new SeatDTO(seats.get(0)));
        dto.setSeats(newSeatDTOs);

        assertEquals(10L, dto.getId());
        assertEquals("Updated Room", dto.getName());
        assertEquals(1, dto.getSeats().size());
        assertEquals(seats.get(0).getId(), dto.getSeats().get(0).getId());
    }
}
