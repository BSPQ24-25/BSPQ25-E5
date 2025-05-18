package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningTest {

    @Test
    void testDefaultConstructor() {
        Screening screening = new Screening();
        assertNotNull(screening.getReservations());
        assertTrue(screening.getReservations().isEmpty());
    }

    @Test
    void testConstructorWithAllFields() {
        Movie movie = new Movie();
        Room room = new Room("Room A");
        Reservation res1 = new Reservation();
        Reservation res2 = new Reservation();
        List<Reservation> reservations = List.of(res1, res2);

        Screening screening = new Screening(movie, "2025-06-01", "Main Hall", room, reservations);

        assertEquals(movie, screening.getMovie());
        assertEquals("2025-06-01", screening.getDate());
        assertEquals("Main Hall", screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertEquals(2, screening.getReservations().size());
    }

    @Test
    void testConstructorWithoutReservationList() {
        Movie movie = new Movie();
        Room room = new Room("Room B");

        Screening screening = new Screening(movie, "2025-06-02", "Side Hall", room);

        assertEquals(movie, screening.getMovie());
        assertEquals("2025-06-02", screening.getDate());
        assertEquals("Side Hall", screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertNotNull(screening.getReservations());
        assertTrue(screening.getReservations().isEmpty());
    }

    @Test
    void testAddReservation() {
        Screening screening = new Screening();
        Reservation reservation = new Reservation();

        screening.addReservation(reservation);

        assertTrue(screening.getReservations().contains(reservation));
        assertEquals(screening, reservation.getScreening());
    }

    @Test
    void testSettersAndGetters() {
        Screening screening = new Screening();

        Movie movie = new Movie();
        Room room = new Room("Room C");
        List<Reservation> reservations = new ArrayList<>();

        screening.setId(1L);
        screening.setMovie(movie);
        screening.setDate("2025-06-03");
        screening.setLocation("VIP Lounge");
        screening.setRoom(room);
        screening.setReservations(reservations);

        assertEquals(1L, screening.getId());
        assertEquals(movie, screening.getMovie());
        assertEquals("2025-06-03", screening.getDate());
        assertEquals("VIP Lounge", screening.getLocation());
        assertEquals(room, screening.getRoom());
        assertEquals(reservations, screening.getReservations());
    }
}
