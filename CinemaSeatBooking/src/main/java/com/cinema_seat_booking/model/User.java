package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * @class User
 * @brief Represents a user of the cinema seat booking system.
 *
 * @details
 * The {@code User} entity models users who can make reservations in the system.
 * It stores authentication information, user role, and the list of reservations
 * made by the user.
 * 
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * @brief Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief Unique username for login.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * @brief Password for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * @brief Email address of the user.
     */
    @Column(nullable = false)
    private String email;

    /**
     * @brief Role of the user (e.g., ADMIN, CLIENT).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * @brief List of reservations made by the user.
     *
     * @details
     * One user can have many reservations.
     * Cascade all operations, and fetch eagerly.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reservation> reservations;

    /**
     * @brief Gets the unique ID of the user.
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Gets the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @brief Gets the password.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @brief Gets the email address.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @brief Gets the role of the user.
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @brief Gets the list of reservations made by the user.
     * @return list of reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @brief Sets the unique ID of the user.
     * @param id the user ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Sets the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @brief Sets the password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @brief Sets the email address.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @brief Sets the user role.
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @brief Sets the list of reservations.
     * 
     * @details
     * Marked with @JsonIgnore to avoid serialization recursion issues.
     * 
     * @param reservations the reservations list to set
     */
    @JsonIgnore
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @brief Adds a reservation to the user.
     *
     * @details
     * Adds the reservation to the list and sets this user on the reservation.
     *
     * @param reservation the reservation to add
     */
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        reservation.setUser(this);
    }

    /**
     * @brief Removes a reservation from the user.
     *
     * @details
     * Removes the reservation from the list and clears the user on the reservation.
     *
     * @param reservation the reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        if (this.reservations != null) {
            this.reservations.remove(reservation);
            reservation.setUser(null);
        }
    }

    /**
     * @brief Default constructor required by JPA.
     */
    public User() {
        // Required by JPA
    }

    /**
     * @brief Constructor with all fields.
     * @param username the username
     * @param password the password
     * @param email the email address
     * @param role the user role
     * @param reservations initial list of reservations
     */
    public User(String username, String password, String email, Role role, List<Reservation> reservations) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.reservations = reservations;
    }

    /**
     * @brief Constructor without reservations.
     * @param username the username
     * @param password the password
     * @param email the email address
     * @param role the user role
     */
    public User(String username, String password, String email, Role role) {
        this(username, password, email, role, null);
    }

    /**
     * @brief Constructor with default role CLIENT.
     * @param username the username
     * @param password the password
     * @param email the email address
     */
    public User(String username, String password, String email) {
        this(username, password, email, Role.CLIENT, null);
    }
}
