/**
 * @file ScreeningService.java
 * @brief Service class responsible for managing movie screenings.
 *
 * @details
 * The {@code ScreeningService} class provides methods for retrieving, scheduling,
 * updating, and deleting movie screenings, as well as filtering by movie, date,
 * or location. It ensures associated seat data is initialized when needed.
 *
 * @see Screening
 * @see ScreeningRepository
 * @see com.cinema_seat_booking.model.Movie
 * @see com.cinema_seat_booking.model.Room
 * @see com.cinema_seat_booking.model.Seat
 * 
 * @author 
 * BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.repository.ScreeningRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @class ScreeningService
 * @brief Handles business logic related to movie screenings.
 */
@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;

    /**
     * @brief Retrieves all screenings with their room seats initialized.
     * @return a list of all {@link Screening} instances
     */
    @Transactional
    public List<Screening> getAllScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        screenings.forEach(screening -> screening.getRoom().getSeats().size()); // Eager load seats
        return screenings;
    }

    /**
     * @brief Retrieves a screening by its ID with room seats initialized.
     * @param id the ID of the screening
     * @return an {@link Optional} containing the {@link Screening} if found
     */
    @Transactional
    public Optional<Screening> getScreeningById(Long id) {
        Optional<Screening> screening = screeningRepository.findById(id);
        screening.ifPresent(s -> s.getRoom().getSeats().size()); // Eager load seats
        return screening;
    }

    /**
     * @brief Schedules a new screening.
     * @param screening the {@link Screening} to save
     * @return the saved {@link Screening} object
     */
    public Screening scheduleScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    /**
     * @brief Deletes a screening by its ID.
     * @param id the ID of the screening to delete
     */
    @Transactional
    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }

    /**
     * @brief Retrieves all screenings for a specific movie.
     * @param movieId the ID of the movie
     * @return a list of {@link Screening} instances for the movie
     */
    public List<Screening> getScreeningsByMovie(Long movieId) {
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);
        screenings.forEach(screening -> screening.getRoom().getSeats().size()); // Eager load seats
        return screenings;
    }

    /**
     * @brief Retrieves screenings scheduled for a specific date.
     * @param date the screening date in string format
     * @return a list of {@link Screening} instances matching the date
     */
    public List<Screening> getScreeningsByDate(String date) {
        List<Screening> screenings = screeningRepository.findByDate(date);
        screenings.forEach(screening -> screening.getRoom().getSeats().size()); // Eager load seats
        return screenings;
    }

    /**
     * @brief Retrieves screenings by location.
     * @param location the location string
     * @return a list of {@link Screening} instances at the specified location
     */
    public List<Screening> getScreeningsByLocation(String location) {
        List<Screening> screenings = screeningRepository.findByLocation(location);
        screenings.forEach(screening -> screening.getRoom().getSeats().size()); // Eager load seats
        return screenings;
    }

    /**
     * @brief Updates an existing screening with new information.
     * 
     * @param id the ID of the screening to update
     * @param newScreening the {@link Screening} object containing updated data
     * @return the updated {@link Screening}
     * 
     * @throws Exception if the screening is not found
     */
    @Transactional
    public Screening updateScreening(Long id, Screening newScreening) throws Exception {
        return screeningRepository.findById(id).map(screening -> {
            screening.setMovie(newScreening.getMovie());
            screening.setDate(newScreening.getDate());
            screening.setLocation(newScreening.getLocation());
            return screeningRepository.save(screening);
        }).orElseThrow(() -> new Exception("Screening not found"));
    }
}
