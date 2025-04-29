package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.repository.ScreeningRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Transactional
    public List<Screening> getAllScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        screenings.forEach(screening -> screening.getRoom().getSeats().size());
        return screenings;
    }

    @Transactional
    public Optional<Screening> getScreeningById(Long id) {
        Optional<Screening> screening = screeningRepository.findById(id);
        if (screening.isPresent()) {
            screening.get().getRoom().getSeats().size();
        }
        return screening;
    }

    public Screening scheduleScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    @Transactional
    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }

    public List<Screening> getScreeningsByMovie(Long movieId) {
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);
        System.out.println("Screenings for movie ID " + movieId + ": " + screenings.size());
        screenings.forEach(screening -> screening.getRoom().getSeats().size());
        return screenings;
    }

    public List<Screening> getScreeningsByDate(String date) {
        List<Screening> screenings = screeningRepository.findByDate(date);
        screenings.forEach(screening -> screening.getRoom().getSeats().size());
        return screenings;
    }

    public List<Screening> getScreeningsByLocation(String location) {
        List<Screening> screenings = screeningRepository.findByLocation(location);
        screenings.forEach(screening -> screening.getRoom().getSeats().size());
        return screenings;
    }

    @Transactional
    public Screening updateScreening(Long id, Screening newScreening) throws Exception {
        return screeningRepository.findById(id).map(screening -> {
            screening.setMovie(newScreening.getMovie());
            screening.setDate(newScreening.getDate());
            screening.setLocation(newScreening.getLocation());
            return screeningRepository.save(screening);
        }).orElseThrow(() -> new Exception("Screening not found"));
    }

}
