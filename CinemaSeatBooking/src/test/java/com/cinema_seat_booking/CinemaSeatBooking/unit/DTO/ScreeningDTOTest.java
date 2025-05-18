package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningDTOTest {

    private Movie movie;
    private Room room;
    private List<Seat> seats;
    private Screening screening;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        room = new Room();
        room.setId(10L);
        room.setName("Main Room");

        seats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Seat seat = new Seat();
            seat.setId((long) i);
            seat.setSeatNumber(i);
            seat.setRoom(room);
            seats.add(seat);
        }
        room.setSeats(seats);

        screening = new Screening();
        screening.setId(100L);
        screening.setMovie(movie);
        screening.setDate("2025-12-25");
        screening.setLocation("Bilbao");
        screening.setRoom(room);
    }

    @Test
    void testConstructorWithScreeningEntity() {
        ScreeningDTO dto = new ScreeningDTO(screening);

        assertEquals(screening.getId(), dto.getId());
        assertEquals(screening.getMovie(), dto.getMovie());
        assertEquals(screening.getDate(), dto.getDate());
        assertEquals(screening.getLocation(), dto.getLocation());
        assertNotNull(dto.getRoom());
        assertEquals(screening.getRoom().getId(), dto.getRoom().getId());
        assertEquals(screening.getRoom().getSeats().size(), dto.getRoom().getSeats().size());
    }

    @Test
    void testConstructorWithParams() {
        ScreeningDTO dto = new ScreeningDTO(200L, movie, "2026-01-01", "Madrid", room);

        assertEquals(200L, dto.getId());
        assertEquals(movie, dto.getMovie());
        assertEquals("2026-01-01", dto.getDate());
        assertEquals("Madrid", dto.getLocation());
        assertNotNull(dto.getRoom());
        assertEquals(room.getId(), dto.getRoom().getId());
        assertEquals(room.getSeats().size(), dto.getRoom().getSeats().size());
    }

    @Test
    void testSettersAndGetters() {
        ScreeningDTO dto = new ScreeningDTO(screening);

        dto.setId(300L);
        dto.setDate("2030-02-02");
        dto.setLocation("Barcelona");

        assertEquals(300L, dto.getId());
        assertEquals("2030-02-02", dto.getDate());
        assertEquals("Barcelona", dto.getLocation());

        Movie newMovie = new Movie();
        newMovie.setId(2L);
        newMovie.setTitle("Matrix");
        dto.setMovie(newMovie);

        assertEquals("Matrix", dto.getMovie().getTitle());

        RoomDTO newRoomDTO = new RoomDTO(room);
        dto.setRoom(newRoomDTO);

        assertEquals(room.getId(), dto.getRoom().getId());
    }
}