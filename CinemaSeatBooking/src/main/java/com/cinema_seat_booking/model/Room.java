package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Seat> seats;
	
	@OneToMany(mappedBy = "room", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JsonIgnore
	private List<Screening> screenings;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
    
    public List<Screening> getScreenings() {
        return screenings;
    }
    
    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    public void addSeat(Seat seat) {
        if (this.seats == null) {
            this.seats = new ArrayList<>();
        }
        if (!this.seats.contains(seat)) {
            this.seats.add(seat);
            seat.setRoom(this);
        }
    }
    
    public void addScreening(Screening screening) {
        if (this.screenings == null) {
            this.screenings = new ArrayList<>();
        }
        if (!this.screenings.contains(screening)) {
            this.screenings.add(screening);
            screening.setRoom(this);
        }
    }

    @Transient
    @JsonProperty("seatCount")
    public int getSeatCount() {
        return seats != null ? seats.size() : 0;
    }

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

    // No-argument constructor (needed for JPA)
    public Room() {
        // This constructor is required by JPA
        this.seats = new ArrayList<>();
        this.screenings = new ArrayList<>();
    }

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