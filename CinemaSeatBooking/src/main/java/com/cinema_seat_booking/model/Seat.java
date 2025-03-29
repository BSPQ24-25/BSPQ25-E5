package com.cinema_seat_booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int seatNumber;

    @Column(nullable = false)
    private boolean isReserved = false;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Seat() {
    }

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    public void reserve() {
        this.isReserved = true;
    }

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

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
