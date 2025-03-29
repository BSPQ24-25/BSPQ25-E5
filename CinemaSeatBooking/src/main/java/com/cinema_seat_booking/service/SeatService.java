package com.cinema_seat_booking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.SeatRepository;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

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
