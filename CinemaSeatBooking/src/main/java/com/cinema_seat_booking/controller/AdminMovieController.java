package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/movies")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "admin/admin-movies";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "admin/admin-movie-form";
    }

    @PostMapping
    public String save(@ModelAttribute("movie") Movie movie) {
        movieService.save(movie);
        return "redirect:/admin/movies";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.findById(id)
            .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        model.addAttribute("movie", movie);
        return "admin/admin-movie-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("movie") Movie movie) {
        movie.setId(id);
        movieService.save(movie);
        return "redirect:/admin/movies";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        movieService.delete(id);
        return "redirect:/admin/movies";
    }
}