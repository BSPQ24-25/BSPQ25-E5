/**
 * @file RoomService.java
 * @brief Service class for managing cinema rooms and their associated seats.
 *
 * @details
 * This service handles business logic for room creation, retrieval, update,
 * and deletion, as well as automatic seat generation upon room creation.
 *
 * @see Room
 * @see Seat
 * @see RoomRepository
 * @see SeatRepository
 * 
 * @author 
 * BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @class RoomService
 * @brief Provides functionality to manage rooms and seats.
 *
 * Handles creation, update, deletion, and retrieval of rooms and automatically
 * generates associated seats when a room is created.
 */
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    /**
     * @brief Creates a new room and generates seats for it.
     *
     * @param name the name of the room
     * @param seatCount the number of seats to generate (currently fixed at 20 in logic)
     * @return the created {@link Room} with seats assigned
     */
    public Room createRoomWithSeats(String name, int seatCount) {
        Room room = new Room(name);
        roomRepository.save(room);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            seats.add(new Seat(i, room));
        }

        seatRepository.saveAll(seats);
        room.setSeats(seats);
        return roomRepository.save(room);
    }

    /**
     * @brief Retrieves a room by its ID with seats initialized.
     *
     * @param id the ID of the room
     * @return an {@link Optional} containing the room if found
     */
    @Transactional(readOnly = true)
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id).map(room -> {
            room.getSeats().size(); // Force initialization
            return room;
        });
    }

    /**
     * @brief Retrieves all rooms with seat collections initialized.
     *
     * @return list of all {@link Room} objects
     */
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> room.getSeats().size()); // Force initialization
        return rooms;
    }

    /**
     * @brief Deletes a room by its ID.
     *
     * @param id the ID of the room to delete
     */
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    /**
     * @brief Updates the name of an existing room.
     *
     * @param id the ID of the room to update
     * @param newName the new name for the room
     * @return the updated {@link Room}
     *
     * @throws NoSuchElementException if the room is not found
     */
    public Room updateRoom(Long id, String newName) throws NoSuchElementException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with ID: " + id));

        room.setName(newName);
        return roomRepository.save(room);
    }
}
