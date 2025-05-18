/**
 * @package com.cinema_seat_booking.controller
 * @brief REST controller providing endpoints for managing cinema seat operations.
 *
 * This controller handles seat creation, retrieval, updating, deletion,
 * as well as reservation and availability checking, interacting with the {@link SeatService}.
 */
package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.SeatDTO;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.service.SeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @class SeatController
 * @brief REST controller for handling seat management operations.
 *
 * Exposes endpoints for CRUD operations and business logic related to cinema seat reservations.
 */
@RestController
@RequestMapping("/api/seats")
public class SeatController {

    /** Service for seat-related operations. */
    @Autowired
    private SeatService seatService;

    /**
     * Retrieves all seats including their reservation status and room name.
     *
     * @return A list of {@link SeatDTO} objects.
     */
    @Operation(summary = "Get all seats", description = "Returns a list of all seats with room information")
    @GetMapping
    public List<SeatDTO> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        return seats.stream()
                .map(seat -> new SeatDTO(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.isReserved(),
                        seat.getRoom().getName()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a seat by its ID.
     *
     * @param id The seat ID
     * @return A {@link ResponseEntity} containing a {@link SeatDTO} if found, or 404 if not
     */
    @Operation(summary = "Get seat by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seat found"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        try {
            Seat seat = seatService.getSeatById(id).orElseThrow(() -> new RuntimeException("Seat not found"));
            SeatDTO seatDTO = new SeatDTO(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seat.isReserved(),
                    seat.getRoom().getName());
            return ResponseEntity.ok(seatDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new seat associated with a room.
     *
     * @param number The seat number
     * @param roomId The ID of the room the seat belongs to
     * @return A {@link ResponseEntity} containing the created {@link Seat}
     */
    @Operation(summary = "Create a seat", description = "Creates a new seat and links it to a room")
    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestParam int number, @RequestParam Long roomId) {
        try {
            Seat seat = seatService.createSeat(number, roomId);
            return ResponseEntity.status(201).body(seat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Updates an existing seat's number and room.
     *
     * @param id The ID of the seat to update
     * @param number The new seat number
     * @param roomId The new room ID
     * @return A {@link ResponseEntity} containing the updated {@link Seat} or 404 if not found
     */
    @Operation(summary = "Update a seat", description = "Updates the seat number or room")
    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id,
                                           @RequestParam int number,
                                           @RequestParam Long roomId) {
        try {
            Seat updatedSeat = seatService.updateSeat(id, number, roomId);
            return ResponseEntity.ok(updatedSeat);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a seat by ID.
     *
     * @param id The ID of the seat
     * @return HTTP 204 if deleted, 404 if not found
     */
    @Operation(summary = "Delete a seat by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        if (seatService.deleteSeat(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all available (non-reserved) seats for a specific room.
     *
     * @param roomId The room ID
     * @return A list of available {@link Seat} objects
     */
    @Operation(summary = "Get available (non-reserved) seats by room ID")
    @GetMapping("/available")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestParam Long roomId) {
        return ResponseEntity.ok(seatService.getAvailableSeats(roomId));
    }

    /**
     * Reserves a seat by ID.
     *
     * @param id The ID of the seat to reserve
     * @return HTTP 200 if successful, 400 if reservation fails
     */
    @Operation(summary = "Reserve a seat")
    @PutMapping("/{id}/reserve")
    public ResponseEntity<Void> reserveSeat(@PathVariable Long id) {
        try {
            seatService.reserveSeat(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancels a reservation for a specific seat.
     *
     * @param id The ID of the seat
     * @return HTTP 200 if successful, 400 if cancellation fails
     */
    @Operation(summary = "Cancel reservation of a seat")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSeatReservation(@PathVariable Long id) {
        try {
            seatService.cancelSeatReservation(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
