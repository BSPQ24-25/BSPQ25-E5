package com.cinema_seat_booking.service;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.PaymentRepository;
import com.cinema_seat_booking.repository.ReservationRepository;
import com.cinema_seat_booking.repository.ScreeningRepository;
import com.cinema_seat_booking.repository.SeatRepository;
import com.cinema_seat_booking.repository.UserRepository;

import jakarta.transaction.Transactional;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Transactional
    public Reservation createReservation(User user, Screening screening, Seat seat, String paymentMethod) {
        Seat managedSeat = seatRepository.findById(seat.getId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        // Check to see if they are free
        if (managedSeat.isReserved()) {
            throw new IllegalStateException("Seat " + managedSeat.getSeatNumber() + " is already reserved!");
        }

        // go through list to mark them as reserved
        managedSeat.setReserved(true);
        seatRepository.save(managedSeat);

        // create the reservation
        Reservation reservation = new Reservation(user, screening, managedSeat);
        reservation.setReservationState(ReservationState.PENDING);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(res -> {
            res.getScreening().getRoom().getSeats().size();
        });
        return reservations;
    }

    @Transactional
    public List<Reservation> getAllReservationsOfUser(String username) {
        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(res -> {
            res.getScreening().getRoom().getSeats().size();
        });

        reservations.removeIf(reservation -> !reservation.getUser().getUsername().equals(username));
        return reservations;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            User user = reservation.getUser(); // Retrieve the user in the reservation
            Seat seat = reservation.getSeat(); // Retrieve all seats in the reservation
            Screening screening = reservation.getScreening(); // Retrieve the screening in the reservation
            Payment payment = reservation.getPayment(); // Retrieve the payment in the reservation

            user.removeReservation(reservation); // Remove the reservation from the user
            userRepository.save(user);

            screening.getReservations().remove(reservation); // Remove the reservation from the screening
            screeningRepository.save(screening);

            seat.setReserved(false);
            seat.setReservation(null); // Remove the reservation from the seat
            seatRepository.save(seat);

            payment.setReservation(null); // Remove the reservation from the payment
            payment.setStatus(PaymentStatus.FAILED); // Update the payment status
            paymentRepository.save(payment);

            // Delete the reservation
            reservation.setUser(null); // Remove the user from the reservation
            reservation.setScreening(null); // Remove the screening from the reservation
            reservation.setSeat(null); // Remove the seat from the reservation
            reservation.setPayment(null); // Remove the payment from the reservation
            reservation.setReservationState(ReservationState.CANCELLED); // Update the reservation state
            reservationRepository.deleteById(reservation.getId());
        } else {
            throw new IllegalArgumentException("Reservation not found!");
        }
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Payment makePayment(Long reservationId, String paymentMethod, double amount, String date) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        Payment payment = paymentService.processPayment(reservation, paymentMethod, amount, date);
        paymentRepository.save(payment);
        reservation.setPayment(payment);
        reservationRepository.save(reservation);
        return payment;
    }

    public Reservation getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        return reservation;
    }
}
