package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.repository.MovieRepository;
import com.cinema_seat_booking.service.RoomService;
import com.cinema_seat_booking.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontendController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/")
    public String showLoginPage() {
        return "home"; // home.html
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html
    }

    @GetMapping({ "/home" })
    public String showHomePage(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "index"; // index.html
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about-us"; // about-us.html
    }

    @GetMapping("/articles")
    public String showArticlesPage() {
        return "articles"; // articles.html
    }

    @GetMapping("/article")
    public String showArticlePage() {
        return "article"; // article.html
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact-us"; // contact-us.html
    }

    @GetMapping("/rooms")
    public String showRoomsPage(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms"; // rooms.html
    }

    @GetMapping("/rooms/{id}")
    public String showRoomDetails(@PathVariable Long id, Model model) {
        // Obtén la sala usando el ID
        model.addAttribute("room", roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id)));
        return "room"; // room.html
    }

    @GetMapping("/screenings")
    public String showScreeningsPage(Model model) {
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening";
    }

    @GetMapping("/screenings/{id}")
    public String showScreeningDetails(@PathVariable Long id, Model model) {
        // Obtén el screening usando el ID
        model.addAttribute("screening", screeningService.getScreeningById(id)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + id)));
        return "reservation"; // reservation.html
    }
}
