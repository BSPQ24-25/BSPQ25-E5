package com.cinema_seat_booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinema_seat_booking.model.Screening;

import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieId(Long movieId);

    List<Screening> findByRoomId(Long roomId);

    List<Screening> findByDate(String date);

    List<Screening> findByLocation(String location);
}
