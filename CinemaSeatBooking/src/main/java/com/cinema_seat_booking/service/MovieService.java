/**
 * @file MovieService.java
 * @brief Service interface for managing movie-related operations.
 *
 * @details
 * The {@code MovieService} interface defines the contract for CRUD operations 
 * related to the {@link Movie} entity. Implementations of this interface handle
 * business logic for retrieving, creating, updating, and deleting movies within
 * the cinema seat booking application.
 *
 * @see Movie
 *
 * @author
 * Example BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 * @interface MovieService
 * @brief Defines the service contract for movie-related functionality.
 *
 * This interface provides methods to interact with {@link Movie} entities, 
 * including listing all movies, finding by ID, saving, and deleting.
 */
public interface MovieService {

    /**
     * @brief Retrieves all movies.
     *
     * @details
     * Fetches a list of all available movies from the data source.
     *
     * @return a list of all {@link Movie} objects
     */
    List<Movie> getAllMovies();

    /**
     * @brief Finds a movie by its ID.
     *
     * @details
     * Searches for a movie with the given ID. If the movie exists, it is returned
     * wrapped in an {@link Optional}; otherwise, an empty {@code Optional} is returned.
     *
     * @param id the unique identifier of the movie
     * @return an {@link Optional} containing the found {@link Movie}, or empty if not found
     */
    Optional<Movie> findById(Long id);

    /**
     * @brief Saves or updates a movie.
     *
     * @details
     * Persists a new movie to the database or updates an existing one.
     *
     * @param movie the {@link Movie} object to be saved
     * @return the saved {@link Movie} instance
     */
    Movie save(Movie movie);

    /**
     * @brief Deletes a movie by its ID.
     *
     * @details
     * Removes the movie with the specified ID from the data source.
     *
     * @param id the unique identifier of the movie to delete
     */
    void delete(Long id);
}
