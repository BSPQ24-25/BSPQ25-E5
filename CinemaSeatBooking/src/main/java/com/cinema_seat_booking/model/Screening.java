
package com.cinema_seat_booking.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;

    @Column(nullable = false)
    private String date;
    
    @Column(nullable = false)
    private String location;

    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL)
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
    
    // No-argument constructor (needed for JPA)
    public Screening() {
        // This constructor is required by JPA
    }

	public Screening(Movie movie, String date, String location, Room room, List<Reservation> reservations) {
		super();
		this.movie = movie;
		this.date = date;
		this.location = location;
		this.room = room;
		this.reservations = reservations;
	}
    
    public Screening(Movie movie, String date, String location, Room room) {
    	this.movie = movie;
    	this.date = date;
    	this.location = location;
    	this.room = room;
    }
}

