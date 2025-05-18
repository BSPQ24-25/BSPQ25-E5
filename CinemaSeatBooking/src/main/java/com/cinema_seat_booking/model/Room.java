package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @class Room
 * @brief Entity class representing a cinema room where screenings take place.
 *
 * @details
 * The {@code Room} class maps to a database entity representing a cinema room.
 * It contains a list of {@link Seat} objects representing seats in the room
 * and a list of {@link Screening} objects representing scheduled screenings.
 * It provides convenience methods to get counts of total, available, and reserved seats.
 * 
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Room {
    /**
     * @brief Unique identifier for the room.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief The unique name of the room.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * @brief List of seats in the room.
     *
     * @details
     * One-to-many relationship with {@link Seat}.
     * Seats are cascade removed if the room is deleted.
     */
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Seat> seats;

    /**
     * @brief List of screenings scheduled in this room.
     *
     * @details
     * One-to-many relationship with {@link Screening}.
     * Cascade persist and merge operations.
     */
    @OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Screening> screenings;

    /**
     * @brief Gets the unique ID of the room.
     *
     * @return the room ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Sets the unique ID of the room.
     *
     * @param id the room ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Gets the name of the room.
     *
     * @return the room name
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Sets the name of the room.
     *
     * @param name the room name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief Gets the list of seats in the room.
     *
     * @return the list of seats
     */
    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * @brief Sets the list of seats in the room.
     *
     * @param seats the list of seats to set
     */
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    /**
     * @brief Gets the list of screenings in the room.
     *
     * @return the list of screenings
     */
    public List<Screening> getScreenings() {
        return screenings;
    }

    /**
     * @brief Sets the list of screenings in the room.
     *
     * @param screenings the list of screenings to set
     */
    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    /**
     * @brief Adds a seat to the room.
     *
     * @details
     * Adds the seat to the seat list and sets the room on the seat.
     *
     * @param seat the seat to add
     */
    public void addSeat(Seat seat) {
        if (this.seats == null) {
            this.seats = new ArrayList<>();
        }
        if (!this.seats.contains(seat)) {
            this.seats.add(seat);
            seat.setRoom(this);
        }
    }

    /**
     * @brief Adds a screening to the room.
     *
     * @details
     * Adds the screening to the screening list and sets the room on the screening.
     *
     * @param screening the screening to add
     */
    public void addScreening(Screening screening) {
        if (this.screenings == null) {
            this.screenings = new ArrayList<>();
        }
        if (!this.screenings.contains(screening)) {
            this.screenings.add(screening);
            screening.setRoom(this);
        }
    }

    /**
     * @brief Gets the total number of seats in the room.
     *
     * @return the seat count
     */
    @Transient
    @JsonProperty("seatCount")
    public int getSeatCount() {
        return seats != null ? seats.size() : 0;
    }

    /**
     * @brief Gets the number of available (not reserved) seats.
     *
     * @return the count of available seats
     */
    @Transient
    @JsonProperty("availableSeats")
    public int getAvailableSeats() {
        int availableSeats = 0;
        if (seats != null) {
            for (Seat seat : seats) {
                if (!seat.isReserved()) {
                    availableSeats++;
                }
            }
        }
        return availableSeats;
    }

    /**
     * @brief Gets the number of reserved seats.
     *
     * @return the count of reserved seats
     */
    @Transient
    @JsonProperty("reservedSeats")
    public int getReservedSeats() {
        int reservedSeats = 0;
        if (seats != null) {
            for (Seat seat : seats) {
                if (seat.isReserved()) {
                    reservedSeats++;
                }
            }
        }
        return reservedSeats;
    }

    /**
     * @brief Default constructor required by JPA.
     *
     * @details
     * Initializes seats and screenings lists to empty lists.
     */
    public Room() {
        this.seats = new ArrayList<>();
        this.screenings = new ArrayList<>();
    }

    /**
     * @brief Constructor to create a room with a name and initial list of seats.
     *
     * @param name the name of the room
     * @param seats the initial list of seats
     */
    public Room(String name, List<Seat> seats) {
        super();
        this.name = name;
        this.seats = new ArrayList<>();
        this.screenings = new ArrayList<>();

        if (seats != null) {
            for (Seat seat : seats) {
                this.addSeat(seat);
            }
        }
    }

    /**
     * @brief Constructor to create a room with a name and default 20 seats.
     *
     * @param name the name of the room
     */
    public Room(String name) {
        this.name = name;
        this.seats = new ArrayList<>();
        this.screenings = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            Seat seat = new Seat(i, false, this);
            this.seats.add(seat);
        }
    }
}
