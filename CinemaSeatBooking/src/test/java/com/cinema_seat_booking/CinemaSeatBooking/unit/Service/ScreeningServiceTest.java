package com.cinema_seat_booking.CinemaSeatBooking.unit.Service;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.ScreeningRepository;
import com.cinema_seat_booking.service.ScreeningService;

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
class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private ScreeningService screeningService;

    private Room room;
    private Screening screening;

    @BeforeEach
    void setUp() {
        room = new Room("Room A");
        room.setId(1L);
        room.setSeats(List.of(new Seat(1, room), new Seat(2, room)));

        screening = new Screening();
        screening.setId(1L);
        screening.setRoom(room);
        screening.setDate("2025-06-01");
        screening.setLocation("Main Hall");
        screening.setMovie(new Movie("Inception", 148, "Sci-fi", "Leonardo DiCaprio"));
    }

    @Test
    void testGetAllScreenings() {
        // Arrange
        when(screeningRepository.findAll()).thenReturn(List.of(screening));

        // Act
        List<Screening> result = screeningService.getAllScreenings();

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getRoom().getSeats().size());
        verify(screeningRepository).findAll();
    }

    @Test
    void testGetScreeningById_Found() {
        // Arrange
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));

        // Act
        Optional<Screening> result = screeningService.getScreeningById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Main Hall", result.get().getLocation());
    }

    @Test
    void testGetScreeningById_NotFound() {
        when(screeningRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Screening> result = screeningService.getScreeningById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testScheduleScreening() {
        when(screeningRepository.save(any(Screening.class))).thenReturn(screening);

        Screening saved = screeningService.scheduleScreening(screening);

        assertNotNull(saved);
        verify(screeningRepository).save(screening);
    }

    @Test
    void testDeleteScreening() {
        screeningService.deleteScreening(1L);

        verify(screeningRepository).deleteById(1L);
    }

    @Test
    void testGetScreeningsByMovie() {
        when(screeningRepository.findByMovieId(1L)).thenReturn(List.of(screening));

        List<Screening> result = screeningService.getScreeningsByMovie(1L);

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getMovie().getTitle());
    }

    @Test
    void testGetScreeningsByDate() {
        when(screeningRepository.findByDate("2025-06-01")).thenReturn(List.of(screening));

        List<Screening> result = screeningService.getScreeningsByDate("2025-06-01");

        assertEquals(1, result.size());
        assertEquals("2025-06-01", result.get(0).getDate());
    }

    @Test
    void testGetScreeningsByLocation() {
        when(screeningRepository.findByLocation("Main Hall")).thenReturn(List.of(screening));

        List<Screening> result = screeningService.getScreeningsByLocation("Main Hall");

        assertEquals(1, result.size());
        assertEquals("Main Hall", result.get(0).getLocation());
    }

    @Test
    void testUpdateScreening_Success() throws Exception {
        Screening updated = new Screening();
        updated.setMovie(new Movie("Interstellar", 169, "Sci-fi", "Matthew McConaughey"));
        updated.setDate("2025-07-01");
        updated.setLocation("Room B");

        when(screeningRepository.findById(1L)).thenReturn(Optional.of(screening));
        when(screeningRepository.save(any(Screening.class))).thenReturn(screening);

        Screening result = screeningService.updateScreening(1L, updated);

        assertNotNull(result);
        assertEquals("Interstellar", result.getMovie().getTitle());
        assertEquals("2025-07-01", result.getDate());
        assertEquals("Room B", result.getLocation());
    }

    @Test
    void testUpdateScreening_NotFound() {
        when(screeningRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            screeningService.updateScreening(999L, new Screening());
        });

        assertEquals("Screening not found", exception.getMessage());
    }
}