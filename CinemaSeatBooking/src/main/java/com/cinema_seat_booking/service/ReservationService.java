package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;
import com.cinema_seat_booking.repository.SeatRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public Reservation createReservation(User user, Screening screening, Seat seat, String paymentMethod) {
        if (seat.isReserved()) {
            throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already reserved!");
        }
    
        // Mark seat as reserved
        seat.setReserved(true);
        seatRepository.save(seat);
    
        // Create reservation
        Reservation reservation = new Reservation(user, screening, seat);
        reservation.setReservationState(ReservationState.PENDING);
    
        // Create payment
        Payment payment = new Payment(paymentMethod, 100.0, LocalDate.now().toString());
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);
    
        reservation.setPayment(payment);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            Seat seat = reservation.getSeat(); // Retrieve all seats in the reservation
            
            seat.setReserved(false);
            seatRepository.save(seat);

            // Delete the reservation
            reservationRepository.delete(reservation);
        } else {
            throw new IllegalArgumentException("Reservation not found!");
        }
    }
    
    public Reservation createReservation2(User user, Screening screening, Seat seat, String paymentMethod) {
    if (seat.isReserved()) {
        throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already reserved!");
    }

    // Create reservation
    Reservation reservation = new Reservation(user, screening, seat);
    reservation.setReservationState(ReservationState.PENDING);

    // Mark seat as reserved
    seat.setReserved(true);
    seatRepository.save(seat);

    // 💡 Create Payment with a default or current date
    Payment payment = new Payment();
    payment.setPaymentMethod(paymentMethod);
    payment.setAmount(100.0); // Or calculate dynamically
    payment.setPaymentDate(LocalDate.now().toString()); // ✅ THIS LINE IS KEY
    payment.setStatus(PaymentStatus.PENDING); // or COMPLETED if you prefer

    payment = paymentRepository.save(payment);

    reservation.setPayment(payment);
    return reservationRepository.save(reservation);
}


    @Transactional
    public Payment makePayment(Long reservationId, String paymentMethod, double amount, String paymentDate) {
        // Step 1: Find the reservation
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found");
        }
    
        Reservation reservation = optionalReservation.get();
    
        // Step 2: Validate input
        if (paymentDate == null || paymentDate.isBlank()) {
            throw new IllegalArgumentException("Payment date cannot be null or blank");
        }
    
        // Step 3: Create and populate the Payment object
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(amount);
        payment.setPaymentDate(paymentDate); // ✅ This must be set
        payment.setStatus(PaymentStatus.COMPLETED); // or PENDING if you want to mark it first
    
        // Step 4: Save the payment and link it to the reservation
        payment = paymentRepository.save(payment); // Save payment first
        reservation.setPayment(payment);
        reservation.setReservationState(ReservationState.PAID);
    
        reservationRepository.save(reservation); // Then update the reservation
    
        return payment;
    }
    
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }
}
