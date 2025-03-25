package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER); 
        return userRepository.save(user);
    }
}
