package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Seat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int seatNumber;

	@Column(nullable = false)
	private boolean isReserved;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "room_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Room room;
	
	@OneToOne(mappedBy = "seat")
	private Reservation reservation;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Reservation getReservation() {
        return reservation;
    }
    
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    // No-argument constructor (needed for JPA)
    public Seat() {
        // This constructor is required by JPA
    }

    public Seat(int seatNumber, boolean isReserved, Room room) {
        super();
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.room = room;
    }

    public Seat(int seatNumber, Room room) {
        this.seatNumber = seatNumber;
        this.isReserved = false;
        this.room = room;
    }
}