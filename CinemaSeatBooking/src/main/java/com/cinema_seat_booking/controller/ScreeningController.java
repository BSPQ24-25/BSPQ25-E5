package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.service.ScreeningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    @Autowired
    private ScreeningService screeningService;

    @Operation(summary = "Return all screenings", description = "Returns all the screenings available in the app")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping
    
  @PostMapping
  
    public ResponseEntity<ScreeningDTO> createScreening(@RequestBody ScreeningDTO screeningDTO) {
    System.out.println("createScreening method called with ScreeningDTO: " + screeningDTO);
    ScreeningDTO saved = screeningService.saveScreening(screeningDTO);
    System.out.println("Saved ScreeningDto" + screeningDTO);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
}
    public ResponseEntity<List<ScreeningDTO>> getAllScreenings() {
        List<Screening> screenings = screeningService.getAllScreenings();
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(screening -> new ScreeningDTO(screening))
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    @Operation(summary = "Get screening by ID", description = "Retrieves a specific screening by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Screening found"),
            @ApiResponse(responseCode = "404", description = "Screening not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScreeningDTO> getScreeningById(@PathVariable Long id) {
        Optional<Screening> screening = screeningService.getScreeningById(id);
        if (screening.isEmpty()) return ResponseEntity.notFound().build(); 
        ScreeningDTO screeningDTO = new ScreeningDTO(screening.get().getId(), screening.get().getMovie(),
                screening.get().getDate(), screening.get().getLocation(), screening.get().getRoom());
        return screeningDTO != null ? ResponseEntity.ok(screeningDTO) : ResponseEntity.notFound().build();
    }

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

    @Operation(summary = "Get screenings by movie", description = "Retrieves all screenings for a specific movie")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByMovie(@PathVariable Long movieId) {
        List<Screening> screenings = screeningService.getScreeningsByMovie(movieId);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(screening -> new ScreeningDTO(screening))
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    @Operation(summary = "Get screenings by date", description = "Retrieves all screenings for a specific date")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByDate(@PathVariable String date) {
        List<Screening> screenings = screeningService.getScreeningsByDate(date);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(screening -> new ScreeningDTO(screening))
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }

    @Operation(summary = "Get screenings by location", description = "Retrieves all screenings at a specific location")
    @ApiResponse(responseCode = "200", description = "List of screenings retrieved successfully")
    @GetMapping("/location/{location}")
    public ResponseEntity<List<ScreeningDTO>> getScreeningsByLocation(@PathVariable String location) {
        List<Screening> screenings = screeningService.getScreeningsByLocation(location);
        List<ScreeningDTO> screeningDTOs = screenings.stream()
                .map(screening -> new ScreeningDTO(screening))
                .toList();
        return ResponseEntity.ok(screeningDTOs);
    }
}
