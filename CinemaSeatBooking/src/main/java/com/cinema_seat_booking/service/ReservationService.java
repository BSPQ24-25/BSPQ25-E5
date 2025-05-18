/**
 * @file ReservationService.java
 * @brief Service class for managing reservation logic in the cinema booking system.
 *
 * @details
 * This class provides methods to create, cancel, retrieve, and pay for reservations.
 * It interacts with various repositories and services, including {@link SeatRepository},
 * {@link ReservationRepository}, {@link PaymentService}, and others.
 *
 * @see Reservation
 * @see Seat
 * @see Payment
 * @see User
 * @see Screening
 * @see PaymentService
 * @see PaymentStatus
 * @see ReservationState
 * 
 * @author 
 * BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
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

/**
 * @class ReservationService
 * @brief Service for creating, retrieving, and managing reservations.
 *
 * Handles all reservation-related operations including creation, cancellation,
 * and payment integration.
 */
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

    /**
     * @brief Creates a new reservation for a user and a specific seat in a screening.
     *
     * @param user the user making the reservation
     * @param screening the screening to reserve a seat for
     * @param seat the seat to reserve
     * @param paymentMethod the payment method to be used
     * @return the created {@link Reservation} object
     *
     * @throws IllegalArgumentException if the seat is not found
     * @throws IllegalStateException if the seat is already reserved
     */
    @Transactional
    public Reservation createReservation(User user, Screening screening, Seat seat, String paymentMethod) {
        Seat managedSeat = seatRepository.findById(seat.getId())
                .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        if (managedSeat.isReserved()) {
            throw new IllegalStateException("Seat " + managedSeat.getSeatNumber() + " is already reserved!");
        }

        managedSeat.setReserved(true);
        seatRepository.save(managedSeat);

        Reservation reservation = new Reservation(user, screening, managedSeat);
        reservation.setReservationState(ReservationState.PENDING);

        return reservationRepository.save(reservation);
    }

    /**
     * @brief Retrieves all reservations in the system.
     *
     * @return list of all {@link Reservation} objects
     */
    @Transactional
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(res -> {
            res.getScreening().getRoom().getSeats().size();
        });
        return reservations;
    }

    /**
     * @brief Retrieves all reservations associated with a specific user.
     *
     * @param username the username of the user
     * @return list of {@link Reservation} objects for the given user
     */
    @Transactional
    public List<Reservation> getAllReservationsOfUser(String username) {
        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(res -> {
            res.getScreening().getRoom().getSeats().size();
        });

        reservations.removeIf(reservation -> !reservation.getUser().getUsername().equals(username));
        return reservations;
    }

    /**
     * @brief Cancels a reservation and reverts associated entities.
     *
     * @param reservationId the ID of the reservation to cancel
     *
     * @throws IllegalArgumentException if the reservation is not found
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            User user = reservation.getUser();
            Seat seat = reservation.getSeat();
            Screening screening = reservation.getScreening();
            Payment payment = reservation.getPayment();

            user.removeReservation(reservation);
            userRepository.save(user);

            screening.getReservations().remove(reservation);
            screeningRepository.save(screening);

            seat.setReserved(false);
            seat.setReservation(null);
            seatRepository.save(seat);

            payment.setReservation(null);
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            reservation.setUser(null);
            reservation.setScreening(null);
            reservation.setSeat(null);
            reservation.setPayment(null);
            reservation.setReservationState(ReservationState.CANCELLED);
            reservationRepository.deleteById(reservation.getId());
        } else {
            throw new IllegalArgumentException("Reservation not found!");
        }
    }

    /**
     * @brief Saves a pre-created reservation object.
     *
     * @param reservation the {@link Reservation} to save
     * @return the saved {@link Reservation}
     */
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    /**
     * @brief Processes payment for a reservation.
     *
     * @param reservationId the ID of the reservation
     * @param paymentMethod the payment method used
     * @param amount the payment amount
     * @param date the date of payment
     * @return the {@link Payment} object created
     *
     * @throws IllegalArgumentException if the reservation is not found
     */
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

    /**
     * @brief Retrieves a reservation by its ID.
     *
     * @param reservationId the ID of the reservation
     * @return the corresponding {@link Reservation}
     *
     * @throws IllegalArgumentException if the reservation is not found
     */
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }
}
