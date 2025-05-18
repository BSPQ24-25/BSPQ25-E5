/**
 * @package com.cinema_seat_booking.controller
 * @brief This package contains REST controllers for handling user operations in the cinema seat booking system.
 *
 * It includes {@link UserController} for managing user registration, profile management, and authentication.
 */
package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @class UserController
 * @brief REST controller that provides endpoints for user registration, profile viewing, updating, and deletion.
 *
 * This controller interacts with the {@link UserService} to perform business logic and database operations.
 */
@Controller
@RequestMapping("/api/users")
public class UserController {

    /** Service that handles all user-related operations. */
    @Autowired
    private UserService userService;

    /**
     * Registers a new user account.
     *
     * @param userDTO The user registration data (username, email, password)
     * @return A {@link ResponseEntity} containing the registered user and HTTP 201 status
     */
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

    /**
     * Retrieves the user profile by username.
     *
     * @param username The username of the user
     * @return A {@link ResponseEntity} containing the user data and HTTP 200 status, or HTTP 404 if not found
     */
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

    /**
     * Updates the profile information of an existing user.
     *
     * @param userDTO The updated user details
     * @return A {@link ResponseEntity} containing the updated user and HTTP 200 status, or HTTP 400 if update failed
     */
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

    /**
     * Deletes a user account based on username and password.
     *
     * @param username The username of the account
     * @param password The password for verification
     * @return A {@link ResponseEntity} with HTTP 204 if deleted, or HTTP 400 if credentials are invalid
     */
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
