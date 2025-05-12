package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Save a new movie
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Get a movie by ID
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Delete a movie by ID
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
