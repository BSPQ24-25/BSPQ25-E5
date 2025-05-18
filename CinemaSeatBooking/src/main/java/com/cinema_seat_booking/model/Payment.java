package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @class Payment
 * @brief Entity class representing a payment made for a reservation.
 *
 * @details
 * The {@code Payment} class maps to a payment record in the database,
 * storing payment method, amount, date, status, and its associated reservation.
 * It has a one-to-one relationship with the {@link Reservation} entity.
 *
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Payment {
    /**
     * @brief Unique identifier for the payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief The method used to make the payment (e.g., credit card, PayPal).
     */
    @Column(nullable = true)
    private String paymentMethod;

    /**
     * @brief The amount paid.
     */
    @Column(nullable = true)
    private double amount;

    /**
     * @brief The date when the payment was made.
     */
    @Column(nullable = true)
    private String paymentDate;

    /**
     * @brief The status of the payment.
     *
     * @details
     * Uses the {@link PaymentStatus} enum to represent statuses like
     * PENDING, COMPLETED, FAILED, etc.
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * @brief The reservation associated with this payment.
     *
     * @details
     * One-to-one bidirectional relationship mapped by the "payment" field in {@link Reservation}.
     */
    @OneToOne(mappedBy = "payment")
    private Reservation reservation;

    /**
     * @brief Default constructor required by JPA.
     */
    public Payment() {
        // Required by JPA
    }

    /**
     * @brief Constructs a payment with all fields except reservation.
     *
     * @param paymentMethod the payment method
     * @param amount the payment amount
     * @param paymentDate the date of payment
     * @param status the status of the payment
     */
    public Payment(String paymentMethod, double amount, String paymentDate, PaymentStatus status) {
        super();
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    /**
     * @brief Constructs a payment without status.
     *
     * @param paymentMethod the payment method
     * @param amount the payment amount
     * @param paymentDate the date of payment
     */
    public Payment(String paymentMethod, double amount, String paymentDate) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    /**
     * @brief Gets the payment ID.
     *
     * @return the payment ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Sets the payment ID.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Gets the payment method.
     *
     * @return the payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @brief Sets the payment method.
     *
     * @param paymentMethod the payment method to set
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @brief Gets the payment amount.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @brief Sets the payment amount.
     *
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @brief Gets the payment date.
     *
     * @return the payment date
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * @brief Sets the payment date.
     *
     * @param paymentDate the payment date to set
     */
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @brief Gets the payment status.
     *
     * @return the payment status
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * @brief Sets the payment status.
     *
     * @param status the payment status to set
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    /**
     * @brief Gets the associated reservation.
     *
     * @return the reservation linked to this payment
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @brief Sets the associated reservation.
     *
     * @param reservation the reservation to link with this payment
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
