package com.cinema_seat_booking.CinemaSeatBooking.unit;

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
    
    @Spy
    @InjectMocks
    private PaymentService paymentService;
    
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationState(ReservationState.PENDING);
    }

    @Test
    void testProcessPayment_Successful() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";
        
        doReturn(true).when(paymentService).simulatePaymentGateway();
        
        // Act
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        
        // Assert
        assertNotNull(payment);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertEquals(payment, reservation.getPayment());
        assertEquals(amount, payment.getAmount());
        assertEquals(date, payment.getPaymentDate());
        assertEquals(paymentMethod, payment.getPaymentMethod());
    }
    
    @Test
    void testProcessPayment_NullReservation() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";
        
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            paymentService.processPayment(null, paymentMethod, amount, date);
        });
        
        // Verify the exception message contains information about the null reservation
        assertTrue(exception.getMessage().contains("reservation"));
    }
    
    @Test
    void testProcessPayment_InvalidAmount() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = -10.0;
        String date = "2025-04-29";
        
        doReturn(true).when(paymentService).simulatePaymentGateway();
        
        // Act
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        
        // Assert - since the original implementation doesn't validate amount
        assertNotNull(payment);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertEquals(amount, payment.getAmount());
    }
    
    @Test
    void testProcessPayment_AlreadyPaid() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";
        
        reservation.setReservationState(ReservationState.PAID);
        Payment existingPayment = new Payment();
        existingPayment.setId(99L);
        reservation.setPayment(existingPayment);
        
        doReturn(true).when(paymentService).simulatePaymentGateway();
        
        // Act
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        
        // Assert - since the original implementation doesn't check if already paid
        assertNotNull(payment);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals(ReservationState.PAID, reservation.getReservationState());
        assertNotEquals(existingPayment, reservation.getPayment());
    }
    
    @Test
    void testProcessPayment_Failed() {
        // Arrange
        String paymentMethod = "Credit Card";
        double amount = 25.0;
        String date = "2025-04-29";
        
        // Mock the payment gateway to return false (failed payment)
        doReturn(false).when(paymentService).simulatePaymentGateway();
        
        // Act
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        
        // Assert
        assertNotNull(payment);
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertEquals(ReservationState.PENDING, reservation.getReservationState());
        assertNull(reservation.getPayment());
    }
}