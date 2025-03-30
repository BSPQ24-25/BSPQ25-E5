package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Optional<Screening> getScreeningById(Long id) {
        return screeningRepository.findById(id);
    }

    public Screening scheduleScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }

    public List<Screening> getScreeningsByMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId);
    }

    public List<Screening> getScreeningsByDate(String date) {
        return screeningRepository.findByDate(date);
    }

    public List<Screening> getScreeningsByLocation(String location) {
        return screeningRepository.findByLocation(location);
    }

    public Screening updateScreening(Long id, Screening newScreening) throws Exception {
        return screeningRepository.findById(id).map(screening -> {
            screening.setMovie(newScreening.getMovie());
            screening.setDate(newScreening.getDate());
            screening.setLocation(newScreening.getLocation());
            return screeningRepository.save(screening);
        }).orElseThrow(() -> new Exception("Screening not found"));
    }

}
