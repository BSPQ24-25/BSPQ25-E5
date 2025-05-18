package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.CreateRoomDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateRoomDTOTest {

    private CreateRoomDTO dto;

    @BeforeEach
    void setUp() {
        dto = new CreateRoomDTO();
    }

    @Test
    void testDefaultConstructorAndSetters() {
        dto.setName("Room A");
        dto.setSeatCount(30);

        assertEquals("Room A", dto.getName());
        assertEquals(30, dto.getSeatCount());
    }

    @Test
    void testAllArgsConstructor() {
        CreateRoomDTO roomDTO = new CreateRoomDTO("VIP Room", 50);

        assertEquals("VIP Room", roomDTO.getName());
        assertEquals(50, roomDTO.getSeatCount());
    }

    @Test
    void testToString() {
        dto.setName("Test Room");
        dto.setSeatCount(25);

        String expected = "CreateRoomDTO{name='Test Room', seatCount=25}";
        assertEquals(expected, dto.toString());
    }
}