package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @class Reservation
 * @brief Entity class representing a seat reservation made by a user for a screening.
 *
 * @details
 * The {@code Reservation} class maps to a reservation record in the database,
 * linking a {@link User}, a {@link Screening}, a {@link Seat}, and a {@link Payment}.
 * It tracks the reservation state and enforces cascading delete behavior.
 * 
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Reservation {
    /**
     * @brief Unique identifier for the reservation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief The user who made the reservation.
     *
     * @details
     * Many-to-one relationship to the {@link User} entity.
     * On user deletion, cascade deletion of reservations.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /**
     * @brief The screening for which the reservation is made.
     *
     * @details
     * Many-to-one relationship to the {@link Screening} entity.
     * On screening deletion, cascade deletion of reservations.
     */
    @ManyToOne
    @JoinColumn(name = "screening_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Screening screening;

    /**
     * @brief The seat reserved.
     *
     * @details
     * One-to-one relationship to the {@link Seat} entity.
     * Cascade all changes to the seat.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    /**
     * @brief The state of the reservation.
     *
     * @details
     * Uses the {@link ReservationState} enum to represent states like PENDING, CONFIRMED, CANCELLED.
     */
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    /**
     * @brief The payment associated with this reservation.
     *
     * @details
     * One-to-one relationship with the {@link Payment} entity.
     * Cascade all changes to the payment.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    /**
     * @brief Gets the reservation ID.
     *
     * @return the reservation ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Gets the user who made the reservation.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @brief Gets the screening linked to this reservation.
     *
     * @return the screening
     */
    public Screening getScreening() {
        return screening;
    }

    /**
     * @brief Gets the reserved seat.
     *
     * @return the seat
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * @brief Gets the reservation state.
     *
     * @return the reservation state
     */
    public ReservationState getReservationState() {
        return reservationState;
    }

    /**
     * @brief Gets the payment linked to this reservation.
     *
     * @return the payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * @brief Sets the reservation ID.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Sets the user for the reservation.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @brief Sets the screening for the reservation.
     *
     * @param screening the screening to set
     */
    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    /**
     * @brief Sets the seat for the reservation.
     *
     * @details
     * Marks the seat as reserved and links back to this reservation.
     *
     * @param seat the seat to set
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
        if (seat != null) {
            seat.setReserved(true);
            seat.setReservation(this);
        }
    }

    /**
     * @brief Sets the reservation state.
     *
     * @param reservationState the reservation state to set
     */
    public void setReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    /**
     * @brief Sets the payment for the reservation.
     *
     * @details
     * Links the payment back to this reservation.
     *
     * @param payment the payment to set
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment != null) {
            payment.setReservation(this);
        }
    }

    /**
     * @brief Default constructor required by JPA.
     *
     * @details
     * Initializes reservation state to PENDING.
     */
    public Reservation() {
        this.reservationState = ReservationState.PENDING;
    }

    /**
     * @brief Constructs a reservation with all main entities.
     *
     * @param user the user making the reservation
     * @param screening the screening reserved
     * @param payment the payment associated
     * @param seat the seat reserved
     */
    public Reservation(User user, Screening screening, Payment payment, Seat seat) {
        this.user = user;
        this.screening = screening;
        this.reservationState = ReservationState.PENDING;
        this.setPayment(payment);
        this.setSeat(seat);
    }

    /**
     * @brief Constructs a reservation without a payment, creates a new payment instance.
     *
     * @param user the user making the reservation
     * @param screening the screening reserved
     * @param seat the seat reserved
     */
    public Reservation(User user, Screening screening, Seat seat) {
        this.user = user;
        this.screening = screening;
        this.reservationState = ReservationState.PENDING;

        Payment newPayment = new Payment();
        this.payment = newPayment;
        newPayment.setReservation(this);

        this.setSeat(seat);
    }
}
