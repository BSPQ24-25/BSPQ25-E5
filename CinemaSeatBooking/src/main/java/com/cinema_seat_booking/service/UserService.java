package com.cinema_seat_booking.service;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;


    @Transactional
    public User registerUser(UserDTO user) {
        try{
            log.info("Registering user: {}", user);

    	User newUser = new User();
    	newUser.setUsername(user.getUsername());
    	newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRole(Role.CLIENT);
        return userRepository.save(newUser);
    } catch (Exception e) {
        log.error("Error registering user: {}", e.getMessage(), e);
        throw e;
    }
    }
    

    public User getUserByUsername(String username) {
        if ((User)userRepository.findByUsername(username) == null) {
            throw new RuntimeException("User not found");
        }
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUserProfile(UserDTO userDTO) {
        User existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser != null) {
            existingUser.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                existingUser.setPassword(userDTO.getPassword());
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

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
}