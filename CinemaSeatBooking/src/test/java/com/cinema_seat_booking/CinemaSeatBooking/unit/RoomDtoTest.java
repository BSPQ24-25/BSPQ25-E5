package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.dto.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomDTOTest {

    @Test
    void testRoomDTOConstructorWithRoom() {
        // Arrange
        Room room = new Room("Room1");
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            seats.add(new Seat(i, room));
        }
        room.setSeats(seats);

        // Act
        RoomDTO roomDTO = new RoomDTO(room);

        // Assert
        assertEquals(room.getId(), roomDTO.getId());
        assertEquals(room.getName(), roomDTO.getName());
        assertEquals(seats.size(), roomDTO.getSeats().size());
    }

    @Test
    void testRoomDTOConstructorWithFields() {
        // Arrange
        List<Seat> seats = new ArrayList<>();
        Room room = new Room("Room1",seats);
        for (int i = 1; i <= 5; i++) {
            seats.add(new Seat(i, room));
        }
        RoomDTO roomDTO = new RoomDTO(room);
        // Act
        // roomDTO = new RoomDTO(1L, "Room1", seats);

        // Assert
        assertEquals(1L, roomDTO.getId());
        assertEquals("Room1", roomDTO.getName());
        assertEquals(seats.size(), roomDTO.getSeats().size());
    }

    @Test
    void testRoomDTOSettersAndGetters() {
        // Arrange
        RoomDTO roomDTO = new RoomDTO(1L, "Room1", new ArrayList<>());

        // Act
        roomDTO.setId(2L);
        roomDTO.setName("Room2");
        List<SeatDTO> seatDTOs = new ArrayList<>();
        seatDTOs.add(new SeatDTO(1L, 1, false, "Room2"));
        roomDTO.setSeats(seatDTOs);

        // Assert
        assertEquals(2L, roomDTO.getId());
        assertEquals("Room2", roomDTO.getName());
        assertEquals(seatDTOs, roomDTO.getSeats());
    }
}