package com.cinema_seat_booking.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinema_seat_booking.dto.CreateRoomDTO;
import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Operation(summary = "Create a new room", description = "Creates a room with 20 seats")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Room created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomDTO createRoomDTO) {
        try {
            System.out.println("Received request to create room: " + createRoomDTO);
            String roomName = createRoomDTO.getName();
            System.out.println("Creating room with name: " + roomName);
            Room createdRoom = roomService.createRoomWithSeats(roomName);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get a room by ID", description = "Returns a room and its seats by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room found"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        RoomDTO roomDTO = new RoomDTO(room.get().getId(), room.get().getName(), room.get().getSeats());
        return roomDTO != null ? ResponseEntity.ok(roomDTO) : ResponseEntity.notFound().build();
    }

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
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
