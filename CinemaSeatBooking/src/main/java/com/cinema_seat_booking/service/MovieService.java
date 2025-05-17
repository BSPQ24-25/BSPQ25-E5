package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Movie;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAllMovies();
    Optional<Movie> findById(Long id);
    Movie save(Movie movie);
    void delete(Long id);
}
