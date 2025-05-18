package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
public class Screening {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "movie_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Movie movie;

	@Column(nullable = false)
	private String date;

	@Column(nullable = false)
	private String location;

	@ManyToOne
	@JoinColumn(name = "room_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Room room;

	@OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Reservation> reservations;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
        reservation.setScreening(this);
    }

    // No-argument constructor (needed for JPA)
    public Screening() {
        // This constructor is required by JPA
        this.reservations = new ArrayList<>();
    }

    public Screening(Movie movie, String date, String location, Room room, List<Reservation> reservations) {
        super();
        this.movie = movie;
        this.date = date;
        this.location = location;
        this.room = room;
        this.reservations = reservations != null ? reservations : new ArrayList<>();
    }

    public Screening(Movie movie, String date, String location, Room room) {
        this.movie = movie;
        this.date = date;
        this.location = location;
        this.room = room;
        this.reservations = new ArrayList<>();
    }
}