package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user by providing user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "View user profile", description = "Fetch the profile of a user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<User> viewProfile(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update user profile", description = "Update the profile details of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or update failed")
    })
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUserProfile(userDTO);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete user profile", description = "Deletes a user profile based on username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User profile successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input or credentials")
    })
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteProfile(@RequestParam String username, @RequestParam String password) {
        boolean deleted = userService.deleteUser(username, password);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

