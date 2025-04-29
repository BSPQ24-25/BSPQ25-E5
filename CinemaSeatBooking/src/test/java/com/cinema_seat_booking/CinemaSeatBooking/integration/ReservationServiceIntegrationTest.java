package com.cinema_seat_booking.CinemaSeatBooking.integration;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.ReservationService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationServiceIntegrationTest {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PaymentRepository paymentRepository;


//     @AfterEach
// @Transactional
// void cleanUpDatabase() {
//     movieRepository.deleteAll();
//     reservationRepository.deleteAll();
//     seatRepository.deleteAll();
//     userRepository.deleteAll();
//     screeningRepository.deleteAll();
// }

    @Test
    void testCreateReservation() {
        // Arrange
        User user = new User();
    user.setEmail("testuser@example.com");       // ✅ Required
    user.setUsername("testuser");                // ✅ ALSO Required
    user.setPassword("securepassword123");       // Optional but usually a good idea
    userRepository.save(user);

    Movie movie = new Movie("Inception2", 1, "thriller", "cast1");
    ArrayList<Seat> seats = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
        Seat seat = new Seat();
        seat.setSeatNumber(i);
        seat.setReserved(false);
        seatRepository.save(seat);
        seats.add(seat);
    }
    Room room = new Room("Room 1", seats);

    Screening screening = new Screening(movie, "2025-04-15 18:00", "Cinema City", room);

    Seat seat = new Seat();
        seat.setSeatNumber(1); // Use an integer for seatNumber
        seat.setReserved(false);
        seatRepository.save(seat);

        // Act
        Reservation reservation = reservationService.createReservation(user, screening, seat, "Credit Card");

        // Assert
        assertNotNull(reservation);
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
        assertTrue(seat.isReserved());
        assertTrue(reservationRepository.findById(reservation.getId()).isPresent());
    }

    @Test
void testCancelReservation() {
    // Arrange
    User user = new User();
    user.setEmail("testuser@example.com");       // ✅ Required
    user.setUsername("testuser");                // ✅ ALSO Required
    user.setPassword("securepassword123");       // Optional but usually a good idea
    userRepository.save(user);


    Movie movie = new Movie("Inception2", 1, "thriller", "cast1");
    movieRepository.save(movie);

    ArrayList<Seat> seats = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
        Seat seat = new Seat();
        seat.setSeatNumber(i);
        seat.setReserved(false);
        seatRepository.save(seat);
        seats.add(seat);
    }

    Room room = new Room("Room Cancel Test", seats);
    roomRepository.save(room);

    Screening screening = new Screening(movie, "2025-04-15 18:00", "Cinema City", room);
    screeningRepository.save(screening);

    Seat seat = seats.get(0);
    
    Reservation reservation = reservationService.createReservation(user, screening, seat, "Credit Card");
    Long reservationId = reservation.getId();

    // Act
    reservationService.cancelReservation(reservationId);

    // Assert
    Optional<Reservation> cancelled = reservationRepository.findById(reservationId);
    assertTrue(cancelled.isEmpty(), "Reservation should be removed after cancellation");

    Seat updatedSeat = seatRepository.findById(seat.getId()).orElseThrow();
    assertFalse(updatedSeat.isReserved(), "Seat should no longer be reserved");
}


    
    @Test
    void testCreateReservation_SeatAlreadyReserved() {
              // Arrange
              User user = new User();
              Movie movie = new Movie("Inception", 1, "thriller", "cast1");
      
              // Initialize the list of seats
              ArrayList<Seat> seats = new ArrayList<>();
              for (int i = 1; i <= 10; i++) { // Add 10 seats to the room
                  Seat seat = new Seat();
                  seat.setSeatNumber(i);
                  seat.setReserved(false);
                  seats.add(seat);
              }
      
              Room room = new Room("Room 3", seats);
              Screening screening = new Screening(movie, "2025-04-15 18:00", "Cinema City", room);
      
              Seat seat = seats.get(0); // Use the first seat from the list
              seat.setReserved(true);
              seatRepository.save(seat);
      
              // Act & Assert
              IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                      reservationService.createReservation(user, screening, seat, "Credit Card")
              );
              assertEquals("Seat 1 is already reserved!", exception.getMessage());
          }
}