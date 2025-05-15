
package com.cinema_seat_booking.controller;

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
    CommandLineRunner initData(UserRepository userRepository, MovieRepository movieRepository,
            RoomRepository roomRepository, SeatRepository seatRepository, ScreeningRepository screeningRepository,
            ReservationRepository reservationRepository, PaymentRepository paymentRepository) {
        return args -> {
            // Database is already initialized
            if (userRepository.count() > 0) {
                logger.info("Database already initialized, skipping data initialization.");
                return;
            }

            // Create some users
            User user1 = new User("john_doe", "password123", "john@example.com");
            User user2 = new User("jane_doe", "password123", "jane@example.com");
            User userAdmin = new User("admin", "admin123", "admin@example.com");

            userRepository.saveAll(List.of(user1, user2, userAdmin));
            logger.info("Users saved!");

            // Create some movies
            Movie movie1 = new Movie("Inception", 148, "Sci-Fi", "Leonardo DiCaprio, Joseph Gordon-Levitt");
            Movie movie2 = new Movie("The Dark Knight", 152, "Action", "Christian Bale, Heath Ledger");
            Movie movie3 = new Movie("Interstellar", 169, "Sci-Fi", "Matthew McConaughey, Anne Hathaway");
            Movie movie4 = new Movie("The Matrix", 136, "Sci-Fi", "Keanu Reeves, Carrie-Anne Moss");
            Movie movie5 = new Movie("The Shawshank Redemption", 142, "Drama", "Tim Robbins, Morgan Freeman");
            Movie movie6 = new Movie("The Godfather", 175, "Crime", "Marlon Brando, Al Pacino");
            Movie movie7 = new Movie("Pulp Fiction", 154, "Crime", "John Travolta, Uma Thurman");
            Movie movie8 = new Movie("The Lord of the Rings: The Return of the King", 201, "Fantasy",
                    "Elijah Wood, Ian McKellen");
            Movie movie9 = new Movie("Forrest Gump", 142, "Drama", "Tom Hanks, Robin Wright");
            Movie movie10 = new Movie("Fight Club", 139, "Drama", "Brad Pitt, Edward Norton");

            movieRepository
                    .saveAll(List.of(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10));
            logger.info("Movies saved!");

            // Create some rooms
            Room room1 = new Room("Room 1");
            Room room2 = new Room("Room 2");
            Room room3 = new Room("Room 3");
            Room room4 = new Room("Room 4");
            Room room5 = new Room("Room 5");
            Room room6 = new Room("Room 6");
            Room room7 = new Room("Room 7");
            Room room8 = new Room("Room 8");
            Room room9 = new Room("Room 9");
            Room room10 = new Room("Room 10");

            roomRepository.saveAll(List.of(room1, room2, room3, room4, room5, room6, room7, room8, room9, room10));
            logger.info("Rooms saved!");

            // Create some screenings
            Screening screening1 = new Screening(movie1, "2024-01-01 18:00", "New York", room1);
            Screening screening2 = new Screening(movie2, "2024-01-02 20:00", "Los Angeles", room2);
            Screening screening3 = new Screening(movie3, "2024-01-03 21:00", "Chicago", room3);
            Screening screening4 = new Screening(movie4, "2024-01-04 19:00", "Houston", room4);
            Screening screening5 = new Screening(movie5, "2024-01-05 17:00", "Phoenix", room5);
            Screening screening6 = new Screening(movie6, "2024-01-06 16:00", "Philadelphia", room6);
            Screening screening7 = new Screening(movie7, "2024-01-07 15:00", "San Antonio", room7);
            Screening screening8 = new Screening(movie8, "2024-01-08 14:00", "San Diego", room8);
            Screening screening9 = new Screening(movie9, "2024-01-09 13:00", "Dallas", room9);
            Screening screening10 = new Screening(movie10, "2024-01-10 12:00", "San Jose", room10);

            screeningRepository.saveAll(List.of(screening1, screening2, screening3, screening4, screening5,
                    screening6, screening7, screening8, screening9, screening10));
            logger.info("Screenings saved!");

        };
    }
}
