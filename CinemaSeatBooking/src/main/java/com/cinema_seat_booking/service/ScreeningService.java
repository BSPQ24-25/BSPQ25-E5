package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.repository.ScreeningRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import com.cinema_seat_booking.dto.*;


import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;


    private Screening convertToEntity(ScreeningDTO screeningDTO) {
        Screening screening = new Screening();
        screening.setId(screeningDTO.getId());
        screening.setMovie(screeningDTO.getMovie());
        screening.setDate(screeningDTO.getDate());
        screening.setLocation(screeningDTO.getLocation());
        screening.setRoom(screeningDTO.getRoom().toEntity()); // Assuming RoomDTO has a `toEntity` method
        return screening;
    }

    // Convert Screening entity to ScreeningDTO
    private ScreeningDTO convertToDTO(Screening screening) {
        ScreeningDTO screeningDTO = new ScreeningDTO();
        screeningDTO.setId(screening.getId());
        screeningDTO.setMovie(screening.getMovie());
        screeningDTO.setDate(screening.getDate());
        screeningDTO.setLocation(screening.getLocation());
        screeningDTO.setRoom(new RoomDTO(screening.getRoom())); // Assuming RoomDTO has a constructor that accepts a Room
        return screeningDTO;
    }

    public ScreeningDTO saveScreening(ScreeningDTO screeningDTO) {
        Screening screening = convertToEntity(screeningDTO); // Convert DTO to entity
        Screening savedScreening = screeningRepository.save(screening); // Save entity
        System.out.println("Saving savescreeningService"+savedScreening);
        return convertToDTO(savedScreening); // Convert saved entity back to DTO
    }
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
