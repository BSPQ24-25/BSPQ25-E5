package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Reservation createReservation(User user, Screening screening, List<Seat> seats, String paymentMethod) {
        // Check to see if they are free 
        for (Seat seat : seats) {
            if (seat.isReserved()) {
                throw new IllegalStateException("Seat " + seat.getSeatNumber() + " is already reserved!");
            }
        }

        //go through list to mark them as reserved 
        for (Seat seat : seats) {
            seat.setReserved(true);
            seatRepository.save(seat);
        }

        //create the reservation 
        Reservation reservation = new Reservation(user, screening, seats, paymentMethod);
        reservation.setReservationState(Reservation.ReservationState.PENDING);

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            List<Seat> seats = reservation.getSeats(); // Retrieve all seats in the reservation

            // Mark all seats as available again
            for (Seat seat : seats) {
                seat.setReserved(false);
                seatRepository.save(seat);
            }

            // Delete the reservation
            reservationRepository.delete(reservation);
        } else {
            throw new IllegalArgumentException("Reservation not found!");
        }
    }
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Payment makePayment(Long reservationId, String paymentMethod, double amount) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount);
        paymentRepository.save(payment);
        reservation.setPayment(payment);
        reservationRepository.save(reservation);
        return payment;
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }
}
