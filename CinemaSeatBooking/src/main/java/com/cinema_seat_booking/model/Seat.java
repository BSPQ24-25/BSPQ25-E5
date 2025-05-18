package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @class Seat
 * @brief Represents a seat within a cinema room.
 *
 * @details
 * The {@code Seat} entity represents an individual seat in a {@link Room}. It tracks
 * the seat number, reservation status, and maintains relationships with the {@link Room}
 * it belongs to and the {@link Reservation} (if any) linked to this seat.
 * 
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Seat {
    /**
     * @brief Unique identifier for the seat.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief The seat number within the room.
     */
    @Column(nullable = false)
    private int seatNumber;

    /**
     * @brief Whether this seat is currently reserved.
     */
    @Column(nullable = false)
    private boolean isReserved;

    /**
     * @brief The room this seat belongs to.
     *
     * @details
     * Many seats belong to one room.
     * Cascade delete is enabled, so deleting a room deletes its seats.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    /**
     * @brief The reservation associated with this seat, if any.
     *
     * @details
     * One seat may be linked to one reservation.
     */
    @OneToOne(mappedBy = "seat")
    private Reservation reservation;

    /**
     * @brief Gets the unique ID of the seat.
     * @return the seat ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Sets the unique ID of the seat.
     * @param id the seat ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Gets the seat number.
     * @return the seat number
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * @brief Sets the seat number.
     * @param seatNumber the seat number to set
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * @brief Checks if the seat is reserved.
     * @return true if reserved, false otherwise
     */
    public boolean isReserved() {
        return isReserved;
    }

    /**
     * @brief Sets the reserved status of the seat.
     * @param isReserved true to mark as reserved, false otherwise
     */
    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    /**
     * @brief Gets the room of the seat.
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @brief Sets the room for this seat.
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @brief Gets the reservation associated with this seat.
     * @return the reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * @brief Sets the reservation for this seat.
     * @param reservation the reservation to set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * @brief Default constructor required by JPA.
     */
    public Seat() {
        // Required by JPA
    }

    /**
     * @brief Constructor with all fields except id.
     * @param seatNumber the seat number
     * @param isReserved the reserved status
     * @param room the room this seat belongs to
     */
    public Seat(int seatNumber, boolean isReserved, Room room) {
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.room = room;
    }

    /**
     * @brief Constructor without reserved status (defaults to false).
     * @param seatNumber the seat number
     * @param room the room this seat belongs to
     */
    public Seat(int seatNumber, Room room) {
        this(seatNumber, false, room);
    }
}
