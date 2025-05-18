package com.cinema_seat_booking.CinemaSeatBooking.unit.Service;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.repository.MovieRepository;
import com.cinema_seat_booking.service.MovieServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setDuration(148);
    }

    @Test
    void testGetAllMovies() {
        // Arrange
        List<Movie> movies = List.of(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        // Act
        List<Movie> result = movieService.getAllMovies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(movie, result.get(0));
        verify(movieRepository).findAll();
    }

    @Test
    void testFindById_Found() {
        // Arrange
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // Act
        Optional<Movie> result = movieService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(movie, result.get());
        verify(movieRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieService.findById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(movieRepository).findById(99L);
    }

    @Test
    void testSaveMovie() {
        // Arrange
        when(movieRepository.save(movie)).thenReturn(movie);

        // Act
        Movie result = movieService.save(movie);

        // Assert
        assertNotNull(result);
        assertEquals(movie, result);
        verify(movieRepository).save(movie);
    }

    @Test
    void testDeleteMovie() {
        // Act
        movieService.delete(1L);

        // Assert
        verify(movieRepository).deleteById(1L);
    }
}
