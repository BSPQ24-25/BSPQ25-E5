
package com.cinema_seat_booking.model;

import jakarta.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private int seatNumber;
    
    @Column(nullable = false)
    private boolean isReserved;

    @ManyToOne
    private Room room;

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

    // No-argument constructor (needed for JPA)
    public Seat() {
        // This constructor is required by JPA
    }
    
	public Seat(int seatNumber, boolean isReserved, Room room) {
		super();
		this.seatNumber = seatNumber;
		this.isReserved = isReserved;
		this.room = room;
		room.getSeats().add(this);
	}
    
	public Seat(int seatNumber, Room room) {
		this.seatNumber=seatNumber;
		this.isReserved=false;
		this.room=room;
		room.getSeats().add(this);
	}
}
