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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    /*
    @PostMapping("/login")
    public String login(@RequestBody User user, HttpSession session) {
        User storedUser = userRepository.findByUsername(user.getUsername());
        if (storedUser != null && user.getPassword().equals(storedUser.getPassword())) {
            String token = Long.toHexString(System.currentTimeMillis());
            session.setAttribute("user", storedUser);
            return token;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    */

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest().body("Username and password are required.");
            }

            User storedUser = userRepository.findByUsername(username);

            if (storedUser == null || !storedUser.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            session.setAttribute("user", storedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("username", storedUser.getUsername());
            response.put("role", storedUser.getRole().name());
            response.put("email", storedUser.getEmail());
            response.put("token", Long.toHexString(System.currentTimeMillis())); // simple token

            return ResponseEntity.ok(response);
        }

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody User user) {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }

            // Asignar CLIENT como rol por defecto
            if (user.getRole() == null) {
                user.setRole(Role.CLIENT);
            }

            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        }

        @PostMapping("/logout")
        public String logout(HttpSession session) {
            session.invalidate();
            return "redirect:/";
        }
    }




