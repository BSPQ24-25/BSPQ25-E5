package com.cinema_seat_booking.CinemaSeatBooking;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;
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

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        payment = new Payment();
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Reservation result = reservationService.getReservationById(1L);
        assertEquals(reservation, result);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationById(2L));
    }

    @Test
    void testMakePayment() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(paymentService.processPayment(eq(reservation), anyString(), anyDouble(), anyString()))
                .thenReturn(payment);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = reservationService.makePayment(1L, "PayPal", 25.0, "2025-04-24");
        assertEquals(payment, result);
        verify(paymentRepository).save(payment);
        verify(reservationRepository).save(reservation);
    }
}

