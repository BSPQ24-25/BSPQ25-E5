package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.User;

import com.cinema_seat_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User storedUser = userRepository.findByUsername(user.getUsername());
        if (storedUser != null && user.getPassword().equals(storedUser.getPassword())) {
        	String token = Long.toHexString(System.currentTimeMillis());
        	return token;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
