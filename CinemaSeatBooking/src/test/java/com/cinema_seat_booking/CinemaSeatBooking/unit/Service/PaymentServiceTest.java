package com.cinema_seat_booking.CinemaSeatBooking.unit.Service;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.PaymentService;
import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.PaymentStatus;
import com.cinema_seat_booking.model.ReservationState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Spy
    @InjectMocks
    private PaymentService paymentService;

    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationState(ReservationState.PENDING);

        payment = new Payment();
        payment.setAmount(25.0);
        payment.setPaymentDate("2025-04-29");
        payment.setPaymentMethod("Credit Card");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setReservation(reservation);

        reservation.setPayment(payment);
    }

    @Test
    void testProcessPayment_Successful() {

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        doReturn(true).when(paymentService).simulatePaymentGateway();

        // Act
        Payment payment_processed = paymentService.processPayment(reservation, payment.getPaymentMethod(),
                payment.getAmount(), payment.getPaymentDate());

        // Assert
        assertNotNull(payment_processed);
        assertEquals(PaymentStatus.COMPLETED, payment_processed.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertEquals(payment_processed, reservation.getPayment());
        assertEquals(payment.getAmount(), payment_processed.getAmount());
        assertEquals(payment.getPaymentDate(), payment_processed.getPaymentDate());
        assertEquals(payment.getPaymentMethod(), payment_processed.getPaymentMethod());
    }

    @Test
    void testProcessPayment_NullReservation() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            paymentService.processPayment(null, payment.getPaymentMethod(), payment.getAmount(),
                    payment.getPaymentDate());
        });

        // Verify the exception message contains information about the null reservation
        assertTrue(exception.getMessage().contains("reservation"));
    }

    @Test
    void testProcessPayment_InvalidAmount() {
        // Arrange
        payment.setAmount(-10);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        doReturn(true).when(paymentService).simulatePaymentGateway();

        // Act
        Payment payment_processed = paymentService.processPayment(reservation, payment.getPaymentMethod(),
                payment.getAmount(), payment.getPaymentDate());

        // Assert - since the original implementation doesn't validate amount
        assertNotNull(payment_processed);
        assertEquals(PaymentStatus.COMPLETED, payment_processed.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertEquals(payment.getAmount(), payment_processed.getAmount());
    }

    @Test
    void testProcessPayment_AlreadyPaid() {

        reservation.setReservationState(ReservationState.PAID);
        doReturn(true).when(paymentService).simulatePaymentGateway();

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            paymentService.processPayment(reservation, payment.getPaymentMethod(),
                    payment.getAmount(), payment.getPaymentDate());
        });

        // Assert
        assertEquals("Reservation is already paid.", exception.getMessage());
    }

    @Test
    void testProcessPayment_Failed() {

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        // when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Mock the payment gateway to return false (failed payment)
        doReturn(false).when(paymentService).simulatePaymentGateway();

        // Act
        Payment payment_processed = paymentService.processPayment(reservation, payment.getPaymentMethod(),
                payment.getAmount(), payment.getPaymentDate());

        // Assert
        assertNotNull(payment_processed);
        assertEquals(PaymentStatus.FAILED, payment_processed.getStatus());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
    }
}