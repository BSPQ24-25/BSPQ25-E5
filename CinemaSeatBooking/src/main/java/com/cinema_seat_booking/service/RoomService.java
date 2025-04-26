package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    public Room createRoomWithSeats(String name) {
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

    @Transactional(readOnly = true)
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id).map(room -> {
            room.getSeats().size();
            return room;
        });
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> room.getSeats().size());
        return rooms;
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room updateRoom(Long id, String newName) throws NoSuchElementException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with ID: " + id));

        room.setName(newName);
        return roomRepository.save(room);
    }
}
