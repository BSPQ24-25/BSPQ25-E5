package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Reservation> reservations;

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    // Generators
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        reservation.setUser(this);
    }

    public void removeReservation(Reservation reservation) {
        if (this.reservations != null) {
            this.reservations.remove(reservation);
            if (reservation.getUser() == this) {
                reservation.setUser(null);
            }
        }
    }

    // No-argument constructor (needed for JPA)
    public User() {
        // This constructor is required by JPA
        this.reservations = new ArrayList<>();
    }

    public User(String username, String password, String email, Role role, List<Reservation> reservations) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.reservations = reservations != null ? reservations : new ArrayList<>();
    }

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.reservations = new ArrayList<>();
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.CLIENT;
        this.reservations = new ArrayList<>();
    }
}