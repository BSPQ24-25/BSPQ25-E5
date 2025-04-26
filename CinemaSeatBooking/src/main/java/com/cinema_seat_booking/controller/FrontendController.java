package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.service.RoomService;
import com.cinema_seat_booking.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @Autowired private RoomService roomService;
    @Autowired private ScreeningService screeningService;

    @GetMapping("/")
    public String showLoginPage() {
        return "home"; // home.html
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html
    }

    @GetMapping("/home")
    public String showHomePage() {
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

    @GetMapping("/screenings")
    public String showScreeningsPage(Model model) {
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening";
    }
}
