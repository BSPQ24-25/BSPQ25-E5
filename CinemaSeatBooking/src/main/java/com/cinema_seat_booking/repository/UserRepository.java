package com.cinema_seat_booking.repository;

import com.cinema_seat_booking.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
        User findByUsernameAndPassword(String username, String password);
    


}