package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestParam("username") String username,
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
}
