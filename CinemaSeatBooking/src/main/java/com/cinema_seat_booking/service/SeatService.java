/**
 * @file SeatService.java
 * @brief Service class for managing seats in cinema rooms.
 *
 * @details
 * This service handles all CRUD operations and business logic related to
 * seat management, including reservation and availability within rooms.
 *
 * @author
 * BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
package com.cinema_seat_booking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;

/**
 * @class SeatService
 * @brief Service layer for seat operations including creation, updating, deletion, and reservation logic.
 */
@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * @brief Retrieves all seats.
     * @return List of all {@link Seat} entities.
     */
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    /**
     * @brief Retrieves a seat by its ID.
     * @param id The ID of the seat.
     * @return An {@link Optional} containing the seat if found.
     */
    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    /**
     * @brief Creates a new seat in the specified room.
     * @param number The seat number.
     * @param roomId The ID of the room.
     * @return The saved {@link Seat} object.
     * @throws IllegalArgumentException if the room is not found.
     */
    public Seat createSeat(int number, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));
        Seat seat = new Seat(number, room);
        return seatRepository.save(seat);
    }

    /**
     * @brief Updates an existing seat with a new number and room.
     * @param id The ID of the seat to update.
     * @param newNumber The new seat number.
     * @param newRoomId The ID of the new room.
     * @return The updated {@link Seat} object.
     * @throws IllegalArgumentException if the seat or new room is not found.
     */
    public Seat updateSeat(Long id, int newNumber, Long newRoomId) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + id));
        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + newRoomId));
        seat.setSeatNumber(newNumber);
        seat.setRoom(newRoom);
        return seatRepository.save(seat);
    }

    /**
     * @brief Deletes a seat by ID.
     * @param id The ID of the seat.
     * @return true if the seat was deleted, false if it was not found.
     */
    public boolean deleteSeat(Long id) {
        if (seatRepository.existsById(id)) {
            seatRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * @brief Retrieves all available (unreserved) seats in a room.
     * @param roomId The ID of the room.
     * @return List of available {@link Seat} objects.
     */
    public List<Seat> getAvailableSeats(Long roomId) {
        return seatRepository.findByRoomIdAndIsReservedFalse(roomId);
    }

    /**
     * @brief Reserves a seat by ID.
     * @param seatId The ID of the seat to reserve.
     * @throws Exception if the seat is not found or is already reserved.
     */
    public void reserveSeat(Long seatId) throws Exception {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception("Seat not found"));

        if (seat.isReserved()) {
            throw new Exception("Seat is already reserved");
        }

        seat.setReserved(true);
        seatRepository.save(seat);
    }

    /**
     * @brief Cancels a reservation for a seat.
     * @param seatId The ID of the seat to unreserve.
     * @throws Exception if the seat is not found or is not reserved.
     */
    public void cancelSeatReservation(Long seatId) throws Exception {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception("Seat not found"));

        if (!seat.isReserved()) {
            throw new Exception("Seat is not reserved");
        }

        seat.setReserved(false);
        seatRepository.save(seat);
    }
}
