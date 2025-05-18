package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Role;

import com.cinema_seat_booking.model.User;

import com.cinema_seat_booking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

public class AuthController {

    @Autowired
    private UserRepository userRepository;

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





