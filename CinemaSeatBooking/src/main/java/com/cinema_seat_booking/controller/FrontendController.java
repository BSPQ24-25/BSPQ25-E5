package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.repository.MovieRepository;
import com.cinema_seat_booking.service.RoomService;
import com.cinema_seat_booking.service.ScreeningService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
    public String showHomePage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("movies", movieRepository.findAll());
        return "index"; // index.html
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about-us"; // about-us.html
    }

    @GetMapping("/articles")
    public String showArticlesPage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        return "articles"; // articles.html
    }

    @GetMapping("/article")
    public String showArticlePage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        return "article"; // article.html
    }

    @GetMapping("/contact")
    public String showContactPage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        return "contact-us"; // contact-us.html
    }

    @GetMapping("/rooms")
    public String showRoomsPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms"; // rooms.html
    }

    @GetMapping("/rooms/{id}")
    public String showRoomDetails(@PathVariable Long id, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("room", roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id)));
        return "room"; // room.html
    }

    @GetMapping("/screenings")
    public String showScreeningsPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening";
    }

    @GetMapping("/screenings/{id}")
    public String showScreeningDetails(@PathVariable Long id, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("screening", screeningService.getScreeningById(id)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + id)));
        return "reservation"; // reservation.html
    }
}
