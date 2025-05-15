package com.cinema_seat_booking.controller;


import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginPageController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/auth/login")
    public String loginClassic(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User storedUser = userRepository.findByUsername(username);

        if (storedUser == null || !storedUser.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }

        session.setAttribute("user", storedUser);

        if (storedUser.getRole() == Role.ADMIN) {
            return "redirect:/admin-dashboard";
        } else {
            return "redirect:/home";
        }
    }
}
