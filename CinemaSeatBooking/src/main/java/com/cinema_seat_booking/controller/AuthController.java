package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @class AuthController
 * @brief REST controller for user authentication and registration.
 * 
 * Handles user registration and login endpoints.
 */
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user.
     * 
     * If the username already exists, returns a bad request.
     * If no role is specified, assigns the CLIENT role by default.
     *
     * @param user The user object to register, provided in the request body.
     * @return ResponseEntity with success or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        if (user.getRole() == null) {
            user.setRole(Role.CLIENT);
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Authenticates a user with username and password.
     * 
     * On successful authentication, stores the user in the HTTP session.
     * Returns the user's role and username.
     * 
     * @param loginRequest Map containing "username" and "password" keys.
     * @param session HTTP session for storing user info on successful login.
     * @return ResponseEntity containing user info or error message.
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        session.setAttribute("user", user);
        return ResponseEntity.ok(Map.of(
            "role", user.getRole().name(),
            "username", user.getUsername()
        ));
    }

}
