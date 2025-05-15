package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.SeatDTO;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Operation(summary = "Get seats by room ID")
@GetMapping("/room/{roomId}")

   public ResponseEntity<List<SeatDTO>> getSeatsByRoomId(@PathVariable Long roomId) {
    try {
        List<Seat> seats = seatService.getSeatsByRoomId(roomId); // Fetch seats by room ID
        List<SeatDTO> seatDTOs = seats.stream()
                .map(seat -> new SeatDTO(seat)) // Convert Seat entities to SeatDTOs
                .collect(Collectors.toList());
        return ResponseEntity.ok(seatDTOs); // Return the list of SeatDTOs
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle errors
    }
}

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

    @Operation(summary = "Delete a seat by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        if (seatService.deleteSeat(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get available (non-reserved) seats by room ID")
    @GetMapping("/available")
    public ResponseEntity<List<Seat>> getAvailableSeats(@RequestParam Long roomId) {
        return ResponseEntity.ok(seatService.getAvailableSeats(roomId));
    }

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
