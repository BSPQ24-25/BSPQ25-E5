package com.cinema_seat_booking.repository;

import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByScreening(Screening screening);
}
