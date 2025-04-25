package com.cinema_seat_booking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.SeatRepository;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    public Seat createSeat(int number, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));
        Seat seat = new Seat(number, room);
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, int newNumber, Long newRoomId) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with ID: " + id));
        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + newRoomId));
        seat.setSeatNumber(newNumber);
        seat.setRoom(newRoom);
        return seatRepository.save(seat);
    }

    public boolean deleteSeat(Long id) {
        if (seatRepository.existsById(id)) {
            seatRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Seat> getAvailableSeats(Long roomId) {
        return seatRepository.findByRoomIdAndIsReservedFalse(roomId);
    }

    public void reserveSeat(Long seatId) throws Exception {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new Exception("Seat not found"));

        if (seat.isReserved()) {
            throw new Exception("Seat is already reserved");
        }

        seat.setReserved(true);
        seatRepository.save(seat);
    }

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
