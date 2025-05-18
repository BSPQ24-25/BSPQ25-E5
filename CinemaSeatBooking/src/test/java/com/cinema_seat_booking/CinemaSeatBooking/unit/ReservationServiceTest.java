package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.PaymentService;
import com.cinema_seat_booking.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Screening screening;
    private Seat seat;
    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        // Initialize test objects
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setReservations(new ArrayList<>());

        Room room = new Room();
        room.setId(1L);
        room.setName("Room 1");
        room.setSeats(new ArrayList<>());

        screening = new Screening();
        screening.setId(1L);
        screening.setRoom(room);
        screening.setReservations(new ArrayList<>());

        // Initialize seat for most tests
        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(1);
        // Important: do NOT set reserved to true in setup, let individual tests control this
        seat.setReserved(false);
        
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setScreening(screening);
        reservation.setSeat(seat);
        reservation.setReservationState(ReservationState.PENDING);
        
        payment = new Payment();
        payment.setId(1L);
        payment.setReservation(reservation);
        payment.setStatus(PaymentStatus.COMPLETED);
        
        reservation.setPayment(payment);
        
        // Add reservation to the user's list
        user.getReservations().add(reservation);
        
        // Add seat to room
        room.getSeats().add(seat);
        
        // Add reservation to screening
        screening.getReservations().add(reservation);
    }

    @Test
    void testCreateReservation_Success() {
        // Arrange
        // Ensure the seat is NOT reserved initially
        Seat availableSeat = new Seat();
        availableSeat.setId(1L);
        availableSeat.setSeatNumber(1);
        availableSeat.setReserved(false);
        
        when(seatRepository.findById(availableSeat.getId())).thenReturn(Optional.of(availableSeat));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.createReservation(user, screening, availableSeat, "Credit Card");

        // Assert
        assertNotNull(result);
        assertEquals(ReservationState.PENDING, result.getReservationState());
        assertTrue(availableSeat.isReserved());
        verify(seatRepository).save(availableSeat);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_SeatAlreadyReserved() {
        // Arrange
        // Explicitly set the seat as reserved for this test
        seat.setReserved(true);
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));

        // Act & Assert
        // Note: The service is throwing IllegalStateException, so we expect that
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(user, screening, seat, "Credit Card");
        });
        
        assertTrue(exception.getMessage().contains("already reserved"));
    }

    @Test
    void testCancelReservation_Success() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        
        // Make sure all collections are initialized
        seat.setReservation(reservation);
        
        // Act
        reservationService.cancelReservation(1L);

        // Assert
        verify(userRepository).save(user);
        verify(screeningRepository).save(screening);
        verify(seatRepository).save(seat);
        verify(paymentRepository).save(payment);
        verify(reservationRepository).deleteById(1L);
        assertFalse(seat.isReserved());
        assertNull(seat.getReservation());
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    void testMakePayment_Success() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";
        
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(paymentService.processPayment(reservation, paymentMethod, amount, date)).thenReturn(payment);
        
        // Act
        Payment result = reservationService.makePayment(1L, paymentMethod, amount, date);
        
        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        verify(paymentRepository).save(payment);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testGetAllReservations() {
        // Arrange
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);
        
        // Act
        List<Reservation> result = reservationService.getAllReservations();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    void testGetReservationById_Success() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        
        // Act
        Reservation result = reservationService.getReservationById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(reservation, result);
    }

    @Test
    void testGetReservationById_NotFound() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservationById(99L);
        });
        
        assertTrue(exception.getMessage().contains("Reservation not found"));
    }
}