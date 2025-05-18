/**
 * @package com.cinema_seat_booking.controller
 * @brief REST controller for managing movie screening-related operations.
 *
 * This controller provides endpoints for CRUD operations and search functionalities
 * for movie screenings. It interacts with the {@link ScreeningService}.
 */
package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.service.ScreeningService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @class ScreeningController
 * @brief Handles HTTP requests related to movie screenings.
 */
@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    /** Service class for screening logic and data access. */
    @Autowired
    private ScreeningService screeningService;

    /**
     * Retrieves all screenings.
     *
     * @return List of {@link ScreeningDTO} wrapped in {@link ResponseEntity}
     */
    @Operation(summary = "Return all screenings", description = "Returns all the screenings available in the app")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ScreeningDTO>> getAllScreenings() {
        List<Screening> screenings = screeningService.getAllScreenings();
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(ScreeningDTO::new)
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    /**
     * Retrieves a screening by its ID.
     *
     * @param id The ID of the screening
     * @return {@link ScreeningDTO} if found, 404 otherwise
     */
    @Operation(summary = "Get screening by ID", description = "Retrieves a specific screening by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Screening found"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScreeningDTO> getScreeningById(@PathVariable Long id) {
        Optional<Screening> screening = screeningService.getScreeningById(id);
        if (screening.isEmpty()) return ResponseEntity.notFound().build();

        ScreeningDTO screeningDTO = new ScreeningDTO(screening.get());
        return ResponseEntity.ok(screeningDTO);
    }

    /**
     * Schedules a new movie screening.
     *
     * @param screening The screening entity to be created
     * @return Created {@link ScreeningDTO} or HTTP 400 on error
     */
    @Operation(summary = "Schedule a new screening", description = "Creates and schedules a new movie screening")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Screening created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ScreeningDTO> scheduleScreening(@RequestBody Screening screening) {
        try {
            Screening createdScreening = screeningService.scheduleScreening(screening);
            ScreeningDTO screeningDTO = new ScreeningDTO(createdScreening);
            return new ResponseEntity<>(screeningDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates the details of an existing screening.
     *
     * @param id The ID of the screening
     * @param screeningDetails The updated screening data
     * @return Updated {@link ScreeningDTO} or error status
     */
    @Operation(summary = "Update an existing screening", description = "Modifies the details of an existing screening")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Screening updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or update failed"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ScreeningDTO> updateScreening(@PathVariable Long id,
                                                        @RequestBody Screening screeningDetails) {
        try {
            Screening updatedScreening = screeningService.updateScreening(id, screeningDetails);
            ScreeningDTO screeningDTO = new ScreeningDTO(updatedScreening);
            return ResponseEntity.ok(screeningDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a screening by ID.
     *
     * @param id The ID of the screening
     * @return HTTP 204 if successful, 404 if not found
     */
    @Operation(summary = "Delete a screening", description = "Deletes a screening by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Screening deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id) {
        Optional<Screening> screening = screeningService.getScreeningById(id);
        if (screening.isPresent()) {
            screeningService.deleteScreening(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves screenings by movie ID.
     *
     * @param movieId The ID of the movie
     * @return List of {@link ScreeningDTO} for the given movie
     */
    @Operation(summary = "Get screenings by movie", description = "Retrieves all screenings for a specific movie")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByMovie(@PathVariable Long movieId) {
        List<Screening> screenings = screeningService.getScreeningsByMovie(movieId);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(ScreeningDTO::new)
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    /**
     * Retrieves screenings by date.
     *
     * @param date The screening date in string format (e.g., "2025-05-18")
     * @return List of {@link ScreeningDTO} for that date
     */
    @Operation(summary = "Get screenings by date", description = "Retrieves all screenings for a specific date")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByDate(@PathVariable String date) {
        List<Screening> screenings = screeningService.getScreeningsByDate(date);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(ScreeningDTO::new)
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    /**
     * Retrieves screenings by location.
     *
     * @param location The location of the screening (e.g., cinema name or branch)
     * @return List of {@link ScreeningDTO} for that location
     */
    @Operation(summary = "Get screenings by location", description = "Retrieves all screenings at a specific location")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/location/{location}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByLocation(@PathVariable String location) {
        List<Screening> screenings = screeningService.getScreeningsByLocation(location);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(ScreeningDTO::new)
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }
}
