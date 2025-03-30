package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    @Autowired
    private ScreeningService screeningService;

    // Obtener todas las funciones
    @GetMapping
    public List<Screening> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    // Obtener una función por ID
    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(@PathVariable Long id) {
        Optional<Screening> screening = screeningService.getScreeningById(id);
        return screening.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Programar una nueva función
    @PostMapping
    public ResponseEntity<Screening> scheduleScreening(@RequestBody Screening screening) {
        try {
            Screening createdScreening = screeningService.scheduleScreening(screening);
            return ResponseEntity.ok(createdScreening);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar una función existente
    @PutMapping("/{id}")
    public ResponseEntity<Screening> updateScreening(@PathVariable Long id, @RequestBody Screening screeningDetails) {
        try {
            Screening updatedScreening = screeningService.updateScreening(id, screeningDetails);
            return ResponseEntity.ok(updatedScreening);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar una función
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Long id) {
        Optional<Screening> screening = screeningService.getScreeningById(id);
        if (screening.isPresent()) {
            screeningService.deleteScreening(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener funciones por película
    @GetMapping("/movie/{movieId}")
    public List<Screening> getScreeningsByMovie(@PathVariable Long movieId) {
        return screeningService.getScreeningsByMovie(movieId);
    }

    // Obtener funciones por fecha
    @GetMapping("/date/{date}")
    public List<Screening> getScreeningsByDate(@PathVariable String date) {
        return screeningService.getScreeningsByDate(date);
    }

    // Obtener funciones por ubicación
    @GetMapping("/location/{location}")
    public List<Screening> getScreeningsByLocation(@PathVariable String location) {
        return screeningService.getScreeningsByLocation(location);
    }
}
