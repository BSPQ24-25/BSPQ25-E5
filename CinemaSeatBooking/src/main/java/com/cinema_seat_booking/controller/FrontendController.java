package com.cinema_seat_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

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
    public String showRoomsPage() {
        return "rooms"; // rooms.html
    }
}
