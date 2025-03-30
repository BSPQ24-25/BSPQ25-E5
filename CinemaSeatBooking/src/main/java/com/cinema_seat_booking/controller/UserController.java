package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @GetMapping("/profile")
    public User viewProfile(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestBody UserDTO user) {
        return userService.updateUserProfile(user);
    }

    @DeleteMapping("/profile")
    public void deleteProfile(@RequestParam String username, String password) {
        userService.deleteUser(username, password);
    }
}
