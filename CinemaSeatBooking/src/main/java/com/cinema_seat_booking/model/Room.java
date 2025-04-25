
package com.cinema_seat_booking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Seat> seats;

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

    public void addSeat(Seat seat) {
        this.seats.add(seat);
        seat.setRoom(this);
    }

    @Transient
    @JsonProperty("seatCount")
    public int getSeatCount() {
        return seats != null ? seats.size() : 0;
    }

    // No-argument constructor (needed for JPA)
    public Room() {
        // This constructor is required by JPA
    }

    public Room(String name, List<Seat> seats) {
        super();
        this.name = name;
        this.seats = seats;
    }

    public Room(String name) {
        this.name = name;
        this.seats = new ArrayList<Seat>();

        for (int i = 1; i <= 20; i++) {
            Seat seat = new Seat(i, this);
            this.seats.add(seat);
        }
    }
}
