
package com.cinema_seat_booking.CinemaSeatBooking;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;

import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Transactional
    CommandLineRunner initData(UserRepository userRepository, MovieRepository movieRepository, RoomRepository roomRepository, SeatRepository seatRepository, ScreeningRepository screeningRepository, ReservationRepository reservationRepository, PaymentRepository paymentRepository) {
        return args -> {
            // Database is already initialized
            if (userRepository.count() > 0) {
                return;
            }

            // Create some users
            User user1 = new User("john_doe", "password123", "john@example.com");
            User user2 = new User("jane_doe", "password123", "jane@example.com");

            userRepository.saveAll(List.of(user1, user2));
            logger.info("Users saved!");

            // Create some movies
            Movie movie1 = new Movie("Inception", 148, "Sci-Fi", "Leonardo DiCaprio, Joseph Gordon-Levitt");
            Movie movie2 = new Movie("The Dark Knight", 152, "Action", "Christian Bale, Heath Ledger");

            movieRepository.saveAll(List.of(movie1, movie2));
            logger.info("Movies saved!");

            // Create some rooms
            Room room1 = new Room("Room1");
            Room room2 = new Room("Room 2");

            roomRepository.saveAll(List.of(room1, room2));
            logger.info("Rooms saved!");

            // Create some seats
            Seat seat1 = new Seat(1, room1);
            Seat seat2 = new Seat(2, room1);
            Seat seat3 = new Seat(3, room2);
            Seat seat4 = new Seat(4, room2);

            seatRepository.saveAll(List.of(seat1, seat2, seat3, seat4));
            logger.info("Seats saved!");

            // Create some screenings
            Screening screening1 = new Screening(movie1, "New York", "2024-01-01 18:00", room1);
            Screening screening2 = new Screening(movie2, "Los Angeles", "2024-01-02 20:00", room2);

            screeningRepository.saveAll(List.of(screening1, screening2));
            logger.info("Screenings saved!");


            // Create some reservations
            Reservation reservation1 = new Reservation(user1, screening1, null, seat1);
            Reservation reservation2 = new Reservation(user2, screening2, null, seat2);

            reservationRepository.saveAll(List.of(reservation1, reservation2));
            logger.info("Reservations saved!");
            
            // Create some payments
            Payment payment1 = new Payment("Credit Card", 20.0, "2024-01-01 18:00", PaymentStatus.COMPLETED);
            Payment payment2 = new Payment("PayPal", 30.0, "2024-01-02 20:00", PaymentStatus.COMPLETED);
            
            paymentRepository.saveAll(List.of(payment1, payment2));
            logger.info("Payments saved!");
            

            // Link payments to reservations
            reservation1.setPayment(payment1);
            reservation2.setPayment(payment2);

            reservationRepository.saveAll(List.of(reservation1, reservation2));
            logger.info("Reservations updated with payments!");
        };
    }
}

