package com.cinema_seat_booking.controller;

import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.ReservationService;

import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Transactional
    CommandLineRunner initData(UserRepository userRepository, MovieRepository movieRepository,
            RoomRepository roomRepository, SeatRepository seatRepository, ScreeningRepository screeningRepository,
            ReservationRepository reservationRepository, PaymentRepository paymentRepository,
            ReservationService reservationService) {
        return args -> {
            // Database is already initialized
            if (userRepository.count() > 0) {
                logger.info("Database already initialized, skipping data initialization.");
                return;
            }

            // Create some users
            User user1 = new User("john_doe", "password123", "john@example.com");
            User user2 = new User("jane_doe", "password123", "jane@example.com");
            User userAdmin = new User("admin", "admin123", "admin@example.com", Role.STAFF);

            // Save users first
            user1 = userRepository.save(user1);
            user2 = userRepository.save(user2);
            userAdmin = userRepository.save(userAdmin);
            logger.info("Users saved!");

            // Create some movies
            Movie movie1 = new Movie("Inception", 148, "Sci-Fi", "Leonardo DiCaprio, Joseph Gordon-Levitt");
            Movie movie2 = new Movie("The Dark Knight", 152, "Action", "Christian Bale, Heath Ledger");

            // Save movies
            movie1 = movieRepository.save(movie1);
            movie2 = movieRepository.save(movie2);
            logger.info("Movies saved!");

            // Create some rooms with seats
            Room room1 = new Room("Room 1");
            room1 = roomRepository.save(room1);
            
            Room room2 = new Room("Room 2");
            room2 = roomRepository.save(room2);
            logger.info("Rooms saved!");

            // Create some screenings
            Screening screening1 = new Screening(movie1, "2024-01-01 18:00", "New York", room1);
            screening1 = screeningRepository.save(screening1);
            
            Screening screening2 = new Screening(movie2, "2024-01-02 20:00", "Los Angeles", room2);
            screening2 = screeningRepository.save(screening2);
            logger.info("Screenings saved!");

            // The Room class should have created seats - let's get one
            Seat seat = null;
            List<Seat> allSeats = seatRepository.findAll();
            
            for (Seat s : allSeats) {
                if (s.getRoom().getId().equals(room1.getId()) && !s.isReserved()) {
                    seat = s;
                    break;
                }
            }
            
            if (seat != null) {
                try {
                    // Create new payment
                    Payment payment = new Payment("Credit Card", 12.99, "2024-01-01", PaymentStatus.PENDING);
                    payment = paymentRepository.save(payment);
                    
                    // Create reservation directly
                    Reservation reservation = new Reservation();
                    reservation.setUser(user1);
                    reservation.setScreening(screening1);
                    reservation.setReservationState(ReservationState.PENDING);
                    
                    // Save first without seat and payment
                    reservation = reservationRepository.save(reservation);
                    
                    // Get fresh seat instance
                    seat = seatRepository.findById(seat.getId()).orElse(null);
                    if (seat != null) {
                        seat.setReserved(true);
                        seat = seatRepository.save(seat);
                        
                        // Now set the relationships
                        reservation.setSeat(seat);
                        reservation.setPayment(payment);
                        payment.setReservation(reservation);
                        
                        // Save it all
                        payment = paymentRepository.save(payment);
                        reservation = reservationRepository.save(reservation);
                        
                        // Update payment status
                        payment.setStatus(PaymentStatus.COMPLETED);
                        reservation.setReservationState(ReservationState.PAID);
                        payment = paymentRepository.save(payment);
                        reservation = reservationRepository.save(reservation);
                        
                        logger.info("Sample reservation created with payment! ID: {}", reservation.getId());
                    } else {
                        logger.error("Could not find the seat after trying to load it fresh");
                    }
                } catch (Exception e) {
                    logger.error("Error creating reservation: {}", e.getMessage());
                    e.printStackTrace();
                }
            } else {
                logger.error("Could not find any available seat in room1");
            }
        };
    }
}