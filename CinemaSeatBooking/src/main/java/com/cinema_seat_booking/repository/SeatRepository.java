package com.cinema_seat_booking.repository;

import com.cinema_seat_booking.model.Seat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByRoomIdAndIsReservedFalse(Long roomId);

    List<Seat> findByRoomId(Long roomId);
}