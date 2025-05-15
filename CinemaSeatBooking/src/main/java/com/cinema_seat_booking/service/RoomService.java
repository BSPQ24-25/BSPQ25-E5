package com.cinema_seat_booking.service;

import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;
    public void deleteAllRooms() {
    roomRepository.deleteAll();
}

    public Room createRoomWithSeats(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty");
        }

        System.out.println("Creating room with name: " + name);
        Room room = new Room(name);
        Room savedRoom = roomRepository.save(room);
        System.out.println("Room saved: " + savedRoom);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            seats.add(new Seat(i, savedRoom));
        }

        System.out.println("Saving seats: " + seats);
        seatRepository.saveAll(seats);
        System.out.println("Seats saved successfully.");

        savedRoom.setSeats(seats);
        Room finalRoom = roomRepository.save(savedRoom);
        System.out.println("Final saved room with seats: " + finalRoom);
        return finalRoom;
    }

    @Transactional(readOnly = true)
    public Optional<Room> getRoomById(Long id) {
        System.out.println("Fetching room by ID: " + id);
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            System.out.println("Room found: " + room);
            room.getSeats().size(); // Force loading of seats
        } else {
            System.out.println("Room not found with ID: " + id);
        }
        return roomOptional;
    }

    public Room createRoom(RoomDTO roomDTO) {
        System.out.println("Creating room from RoomDTO: " + roomDTO);
        String roomName = roomDTO.getName();
        return createRoomWithSeats(roomName);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        System.out.println("Fetching all rooms...");
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> {
            room.getSeats().size(); // Force loading of seats
            System.out.println("Room loaded: " + room);
        });
        return rooms;
    }

    public void deleteRoom(Long id) {
        System.out.println("Deleting room with ID: " + id);
        roomRepository.deleteById(id);
        System.out.println("Room deleted successfully.");
    }

    public Room updateRoom(Long id, String newName) throws NoSuchElementException {
        System.out.println("Updating room with ID: " + id + " to new name: " + newName);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with ID: " + id));

        room.setName(newName);
        Room updatedRoom = roomRepository.save(room);
        System.out.println("Room updated successfully: " + updatedRoom);
        return updatedRoom;
    }
}