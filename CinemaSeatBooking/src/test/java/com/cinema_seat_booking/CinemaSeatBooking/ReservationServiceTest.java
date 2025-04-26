package com.cinema_seat_booking.CinemaSeatBooking;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import com.cinema_seat_booking.service.PaymentService;
import com.cinema_seat_booking.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Screening screening;
    private Seat seat;
    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        screening = new Screening();
        screening.setId(2L);

        seat = new Seat();
        seat.setId(3L);
        seat.setSeatNumber(5);
        seat.setReserved(false);

        reservation = new Reservation(user, screening, seat);
        reservation.setId(4L);

        payment = new Payment();
    }

    @Test
    void testCreateReservation_Success() {
        when(seatRepository.save(seat)).thenReturn(seat);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation(user, screening, seat, "Credit");

        assertNotNull(result);
        assertEquals(ReservationState.PENDING, result.getReservationState());
        assertTrue(seat.isReserved());
        verify(seatRepository).save(seat);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_SeatAlreadyReserved() {
        seat.setReserved(true);

        IllegalStateException ex = assertThrows(
            IllegalStateException.class,
            () -> reservationService.createReservation(user, screening, seat, "Credit")
        );
        assertTrue(ex.getMessage().contains("already reserved"));
    }

    @Test
    void testCancelReservation_Success() {
        seat.setReserved(true);
        reservation.setSeat(seat);

        when(reservationRepository.findById(4L)).thenReturn(Optional.of(reservation));
        when(seatRepository.save(seat)).thenReturn(seat);
        doNothing().when(reservationRepository).delete(reservation);

        reservationService.cancelReservation(4L);

        assertFalse(seat.isReserved());
        verify(seatRepository).save(seat);
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void testCancelReservation_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> reservationService.cancelReservation(99L)
        );
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(4L)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(4L);

        assertEquals(reservation, result);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> reservationService.getReservationById(99L)
        );
    }

    @Test
    void testMakePayment() {
        when(reservationRepository.findById(4L)).thenReturn(Optional.of(reservation));
        when(paymentService.processPayment(eq(reservation), anyString(), anyDouble(), anyString()))
            .thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Payment result = reservationService.makePayment(4L, "PayPal", 30.0, "2025-04-24");

        assertEquals(payment, result);
        verify(paymentService).processPayment(reservation, "PayPal", 30.0, "2025-04-24");
        verify(paymentRepository).save(payment);
        verify(reservationRepository).save(reservation);
    }
}