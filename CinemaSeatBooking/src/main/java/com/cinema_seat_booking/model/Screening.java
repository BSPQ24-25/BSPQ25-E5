package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

/**
 * @class Screening
 * @brief Represents a movie screening event within a cinema system.
 *
 * @details
 * The {@code Screening} entity represents a scheduled showing of a movie in a specific room at a specific date and location.
 * It maintains relationships with the {@link Movie} being shown, the {@link Room} where it takes place, and the {@link Reservation} entities
 * associated with bookings for this screening.
 *
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Screening {
    /**
     * @brief Unique identifier for the screening.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief Movie associated with the screening.
     *
     * @details
     * Many screenings can be associated with one movie.
     * Cascade delete is enabled, so deleting a movie deletes associated screenings.
     */
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;

    /**
     * @brief Date of the screening.
     */
    @Column(nullable = false)
    private String date;

    /**
     * @brief Location of the screening (e.g., cinema branch or hall name).
     */
    @Column(nullable = false)
    private String location;

    /**
     * @brief Room in which the screening takes place.
     *
     * @details
     * Many screenings can be scheduled in one room.
     * Cascade delete is enabled, so deleting a room deletes associated screenings.
     */
    @ManyToOne
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    /**
     * @brief List of reservations for this screening.
     *
     * @details
     * One screening can have many reservations.
     * Cascade all operations and remove orphans.
     * Ignored in JSON serialization to avoid recursion.
     */
    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    /**
     * @brief Gets the unique ID of the screening.
     * @return the screening ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Sets the unique ID of the screening.
     * @param id the screening ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Gets the movie associated with this screening.
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * @brief Sets the movie associated with this screening.
     * @param movie the movie to set
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * @brief Gets the date of the screening.
     * @return the date string
     */
    public String getDate() {
        return date;
    }

    /**
     * @brief Sets the date of the screening.
     * @param date the date string to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @brief Gets the location of the screening.
     * @return the location string
     */
    public String getLocation() {
        return location;
    }

    /**
     * @brief Sets the location of the screening.
     * @param location the location string to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @brief Gets the room where the screening takes place.
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @brief Sets the room where the screening takes place.
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @brief Gets the list of reservations for this screening.
     * @return list of reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @brief Sets the list of reservations for this screening.
     * @param reservations the reservations list to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @brief Adds a reservation to this screening.
     *
     * @details
     * Adds the reservation to the list and sets this screening on the reservation.
     *
     * @param reservation the reservation to add
     */
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        reservation.setScreening(this);
    }

    /**
     * @brief Default constructor required by JPA.
     *
     * @details Initializes the reservations list.
     */
    public Screening() {
        this.reservations = new ArrayList<>();
    }

    /**
     * @brief Constructor with all fields.
     *
     * @param movie the movie shown in the screening
     * @param date the date of the screening
     * @param location the location of the screening
     * @param room the room where the screening takes place
     * @param reservations the initial list of reservations
     */
    public Screening(Movie movie, String date, String location, Room room, List<Reservation> reservations) {
        super();
        this.movie = movie;
        this.date = date;
        this.location = location;
        this.room = room;
        this.reservations = reservations != null ? reservations : new ArrayList<>();
    }

    /**
     * @brief Constructor without reservations list.
     *
     * @param movie the movie shown in the screening
     * @param date the date of the screening
     * @param location the location of the screening
     * @param room the room where the screening takes place
     */
    public Screening(Movie movie, String date, String location, Room room) {
        this.movie = movie;
        this.date = date;
        this.location = location;
        this.room = room;
        this.reservations = new ArrayList<>();
    }
}
