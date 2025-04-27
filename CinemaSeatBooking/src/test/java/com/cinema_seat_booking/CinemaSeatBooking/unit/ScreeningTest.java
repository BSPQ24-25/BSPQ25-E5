package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.*;


import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningTest {

    @Test
    void testConstructorWithoutReservations() {
        Movie movie = new Movie();
        String date = "2025-04-20";
        String location = "Cinema Hall 1";
        Room room = new Room();

        Screening screening = new Screening(movie, date, location, room);

        assertEquals(movie, screening.getMovie());
        assertEquals(date, screening.getDate());
        assertEquals(location, screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertNull(screening.getReservations());
    }

    @Test
    void testConstructorWithReservations() {
        Movie movie = new Movie();
        String date = "2025-04-21";
        String location = "Cinema Hall 2";
        Room room = new Room();
        List<Reservation> reservations = new ArrayList<>();

        Screening screening = new Screening(movie, date, location, room, reservations);

        assertEquals(movie, screening.getMovie());
        assertEquals(date, screening.getDate());
        assertEquals(location, screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertEquals(reservations, screening.getReservations());
    }

    @Test
    void testSettersAndGetters() {
        Movie movie = new Movie();
        String date = "2025-04-22";
        String location = "Cinema Hall 3";
        Room room = new Room();
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation();
        reservations.add(reservation);

        Screening screening = new Screening();

        screening.setId(10L);
        screening.setMovie(movie);
        screening.setDate(date);
        screening.setLocation(location);
        screening.setRoom(room);
        screening.setReservations(reservations);

        assertEquals(10L, screening.getId());
        assertEquals(movie, screening.getMovie());
        assertEquals(date, screening.getDate());
        assertEquals(location, screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertEquals(reservations, screening.getReservations());
    }
}
