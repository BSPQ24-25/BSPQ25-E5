package com.cinema_seat_booking.CinemaSeatBooking.unit.Service;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import com.cinema_seat_booking.service.RoomService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Room A");
        room.setSeats(new ArrayList<>());
    }

    @Test
    void testCreateRoomWithSeats_Success() {
        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room result = roomService.createRoomWithSeats("Room A", 20);

        // Assert
        assertNotNull(result);
        verify(roomRepository, times(2)).save(any(Room.class)); // once before, once after adding seats
        verify(seatRepository).saveAll(anyList());
    }

    @Test
    void testGetRoomById_Found() {
        // Arrange
        room.setSeats(List.of(new Seat(1, room), new Seat(2, room)));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Act
        Optional<Room> result = roomService.getRoomById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().getSeats().size());
        verify(roomRepository).findById(1L);
    }

    @Test
    void testGetRoomById_NotFound() {
        // Arrange
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Room> result = roomService.getRoomById(99L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllRooms() {
        // Arrange
        Room room1 = new Room("Room 1");
        room1.setSeats(List.of(new Seat(1, room1)));

        Room room2 = new Room("Room 2");
        room2.setSeats(List.of(new Seat(1, room2), new Seat(2, room2)));

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        // Act
        List<Room> result = roomService.getAllRooms();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSeats().size());
        assertEquals(2, result.get(1).getSeats().size());
        verify(roomRepository).findAll();
    }

    @Test
    void testDeleteRoom() {
        // Act
        roomService.deleteRoom(1L);

        // Assert
        verify(roomRepository).deleteById(1L);
    }

    @Test
    void testUpdateRoom_Success() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        Room result = roomService.updateRoom(1L, "Updated Room");

        // Assert
        assertNotNull(result);
        assertEquals("Updated Room", result.getName());
        verify(roomRepository).save(room);
    }

    @Test
    void testUpdateRoom_NotFound() {
        // Arrange
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            roomService.updateRoom(999L, "Doesn't Exist");
        });

        assertTrue(exception.getMessage().contains("Room not found"));
    }
}