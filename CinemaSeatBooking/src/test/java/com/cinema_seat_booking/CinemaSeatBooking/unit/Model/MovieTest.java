package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class MovieTest {

    private Movie movie;

    @BeforeEach
    public void setup() {
        movie = new Movie("Inception", 148, "Sci-Fi", "Leonardo DiCaprio");
    }

    @Test
    public void testConstructorWithoutScreenings() {
        assertEquals("Inception", movie.getTitle());
        assertEquals(148, movie.getDuration());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals("Leonardo DiCaprio", movie.getCast());
        assertNotNull(movie.getScreenings());
        assertTrue(movie.getScreenings().isEmpty());
    }

    @Test
    public void testConstructorWithScreenings() {
        List<Screening> screenings = new ArrayList<>();
        Screening screening = new Screening();
        screenings.add(screening);

        Movie m = new Movie("Matrix", 136, "Action", "Keanu Reeves", screenings);
        assertEquals("Matrix", m.getTitle());
        assertEquals(136, m.getDuration());
        assertEquals("Action", m.getGenre());
        assertEquals("Keanu Reeves", m.getCast());
        assertEquals(1, m.getScreenings().size());
    }

    @Test
    public void testSettersAndGetters() {
        movie.setId(100L);
        movie.setTitle("Interstellar");
        movie.setDuration(169);
        movie.setGenre("Adventure");
        movie.setCast("Matthew McConaughey");

        assertEquals(100L, movie.getId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals(169, movie.getDuration());
        assertEquals("Adventure", movie.getGenre());
        assertEquals("Matthew McConaughey", movie.getCast());
    }

    @Test
    public void testSetScreenings() {
        List<Screening> screenings = new ArrayList<>();
        Screening s = new Screening();
        screenings.add(s);

        movie.setScreenings(screenings);
        assertEquals(1, movie.getScreenings().size());
    }

    @Test
    public void testAddScreening() {
        Screening screening = new Screening();
        movie.addScreening(screening);

        assertEquals(1, movie.getScreenings().size());
        assertSame(movie, screening.getMovie());
    }

    @Test
    public void testAddScreeningWithNullList() {
        Movie m = new Movie("Dune", 155, "Sci-Fi", "Timothée Chalamet");
        m.setScreenings(null);

        Screening screening = new Screening();
        m.addScreening(screening);

        assertNotNull(m.getScreenings());
        assertEquals(1, m.getScreenings().size());
        assertSame(m, screening.getMovie());
    }

}
