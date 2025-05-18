/**
 * @package com.cinema_seat_booking.controller
 * @brief REST controller for managing cinema rooms and their seats.
 *
 * Provides endpoints to create, read, update, and delete rooms,
 * along with retrieving rooms and their associated seats.
 */
package com.cinema_seat_booking.controller;

import java.util.List;
import java.util.Optional;

import com.cinema_seat_booking.dto.CreateRoomDTO;
import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @class RoomController
 * @brief Exposes REST endpoints for managing cinema rooms.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    /** Service for room-related business logic. */
    @Autowired
    private RoomService roomService;

    /**
     * Creates a new room with the specified name and seat count.
     *
     * @param createRoomDTO The DTO containing room name and seat count
     * @return Created {@link Room} wrapped in HTTP 201, or 400 on error
     */
    @Operation(summary = "Create a new room", description = "Creates a room with specified seat count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomDTO createRoomDTO) {
        try {
            Room createdRoom = roomService.createRoomWithSeats(
                    createRoomDTO.getName(), createRoomDTO.getSeatCount());
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves a room and its seats by its ID.
     *
     * @param id The ID of the room
     * @return {@link RoomDTO} if found, or 404 if not
     */
    @Operation(summary = "Get a room by ID", description = "Returns a room and its seats by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            RoomDTO roomDTO = new RoomDTO(room.get().getId(), room.get().getName(), room.get().getSeats());
            return ResponseEntity.ok(roomDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves all rooms with their seat details.
     *
     * @return List of {@link RoomDTO}, or HTTP 204 if empty
     */
    @Operation(summary = "Get all rooms", description = "Returns all available rooms")
    @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully")
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomDTO> roomDTOs = rooms.stream()
                .map(room -> new RoomDTO(room.getId(), room.getName(), room.getSeats()))
                .toList();
        return roomDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roomDTOs);
    }

    /**
     * Deletes a room by its ID.
     *
     * @param id The ID of the room
     * @return HTTP 204 if successful, 404 if room not found
     */
    @Operation(summary = "Delete a room", description = "Deletes a room by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Room deleted"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomService.getRoomById(id).isPresent()) {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the name of a room.
     *
     * @param id The ID of the room to update
     * @param updatedRoom A {@link Room} object containing the new name
     * @return Updated {@link Room}, or appropriate error code
     */
    @Operation(summary = "Update a room", description = "Updates the name of a room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room updated successfully"),
            @ApiResponse(responseCode = "404", description = "Room not found"),
            @ApiResponse(responseCode = "400", description = "Invalid update data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        try {
            Room room = roomService.updateRoom(id, updatedRoom.getName());
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
