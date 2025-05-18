package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @class LoginController
 * @brief REST controller for user login functionality.
 *
 * Provides an endpoint to authenticate users and start a session.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticates a user with the provided username and password.
     * If successful, stores the user in the HTTP session.
     *
     * @param username the username of the user trying to log in
     * @param password the password of the user
     * @param session  the HTTP session for storing authenticated user data
     * @return a ResponseEntity containing the username and role on success,
     *         or an error message on failure
     */
    @PostMapping
    public ResponseEntity<?> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session) {

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Username and password are required.");
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        session.setAttribute("user", user);
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole().name()
        ));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
