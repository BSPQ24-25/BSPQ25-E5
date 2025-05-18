/**
 * @file UserService.java
 * @brief Service class for managing user-related operations in the cinema seat booking system.
 *
 * @details
 * This service provides the business logic for user management including:
 * - User registration with default CLIENT role
 * - Authentication of users via username and password
 * - Updating user profiles such as email and password
 * - Deletion of user accounts with authentication check
 *
 * It interacts with the {@link UserRepository} for persistence operations,
 * and works closely with {@link User} and {@link Role} model classes.
 *
 * @see User
 * @see UserDTO
 * @see Role
 * @see UserRepository
 * 
 * @version 1.0
 * @since 2025-05-19
 * @author 
 * BSPQ25-E5
 */
package com.cinema_seat_booking.service;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @class UserService
 * @brief Business logic for user operations such as registration, authentication, and profile management.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * @brief Registers a new user with CLIENT role.
     * @param user A DTO containing user registration data.
     * @return The saved {@link User} entity.
     */
    @Transactional
    public User registerUser(UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword()); // Consider encrypting the password in production
        newUser.setRole(Role.CLIENT);
        return userRepository.save(newUser);
    }

    /**
     * @brief Retrieves a user by their username.
     * @param username The username to search for.
     * @return The {@link User} if found.
     * @throws RuntimeException if the user is not found.
     */
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    /**
     * @brief Updates the profile of an existing user.
     * @param userDTO DTO containing updated user details.
     * @return The updated {@link User} entity.
     * @throws RuntimeException if the user is not found.
     */
    @Transactional
    public User updateUserProfile(UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser != null) {
            existingUser.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword()); // Again, encryption recommended
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * @brief Deletes a user account if username and password match.
     * @param username The username of the account to delete.
     * @param password The password for authentication.
     * @return true if deletion is successful, false otherwise.
     */
    @Transactional
    public boolean deleteUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            userRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @brief Authenticates a user using username and password.
     * @param username The user's username.
     * @param password The user's password.
     * @return The {@link User} if authentication is successful, otherwise null.
     */
    public User authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
