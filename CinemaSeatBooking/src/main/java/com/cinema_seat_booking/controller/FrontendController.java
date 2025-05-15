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

    private String getUserRole(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj instanceof com.cinema_seat_booking.model.User user) {
        	return user.getRole().name(); // Devuelve "ADMIN" o "CLIENT"
        }
        return null;
    }

    
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return "redirect:/";
        }

        String role = getUserRole(session);
        if ("ADMIN".equals(role)) {
            return "redirect:/admin-dashboard";
        }

        model.addAttribute("movies", movieRepository.findAll());
        return "index"; // Para CLIENT
    }
    
    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        Object userObj = session.getAttribute("user");
        if (userObj == null || !"ADMIN".equals(getUserRole(session))) {
            return "redirect:/";
        }

        model.addAttribute("movies", movieRepository.findAll()); // O lo que necesites
        return "admin-dashboard"; // admin-dashboard.html
    }

    

    @GetMapping("/about")
    public String showAboutPage() {
        return "about-us"; // about-us.html
    }

    @GetMapping("/404-lt")
    public String show404ltPage() {
        return "404-lt"; // about-us.html
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
    //lithuanian
    

    @GetMapping("/register-lt")
    public String showLTRegisterPage() {
        return "register-lt"; // register.html
    }

    @GetMapping({ "/lt", "/home-lt" })
    public String showLTHomePage(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "index-lt"; // index.html
    }

    @GetMapping("/about-lt")
    public String showLTAboutPage() {
        return "about-us-lt"; // about-us.html
    }

    @GetMapping("/articles-lt")
    public String showLTArticlesPage() {
        return "articles-lt"; // articles.html
    }

    @GetMapping("/article-lt")
    public String showLTArticlePage() {
        return "article-lt"; // article.html
    }

    @GetMapping("/contact-lt")
    public String showLTContactPage() {
        return "contact-us-lt"; // contact-us.html
    }

    @GetMapping("/rooms-lt")
    public String showLTRoomsPage(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms-lt"; // rooms.html
    }

    @GetMapping("/screenings-lt")
    public String showLTScreeningsPage(Model model) {
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening-lt";
    }

    @GetMapping("/screenings-lt/{id}")
    public String showLTScreeningDetails(@PathVariable Long id, Model model) {
        // Obtén el screening usando el ID
        model.addAttribute("screening", screeningService.getScreeningById(id)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + id)));
        return "reservation-lt"; // reservation.html
    }
}
