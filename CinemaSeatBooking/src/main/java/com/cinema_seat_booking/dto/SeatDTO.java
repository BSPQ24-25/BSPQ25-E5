package com.cinema_seat_booking.dto;

import com.cinema_seat_booking.model.Seat;

public class SeatDTO {
    private Long id;
    private int seatNumber;
    private boolean isReserved;
    private String roomName;

    public SeatDTO() {
        // Default constructor
    }

    public SeatDTO(Long id, int seatNumber, boolean isReserved, String roomName) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.roomName = roomName;
    }

    public SeatDTO(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.isReserved = seat.isReserved();
        this.roomName = seat.getRoom() != null ? seat.getRoom().getName() : null;
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

    public void setReserved(boolean isReserved) {
        this.isReserved = isReserved;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "SeatDTO{" +
                "id=" + id +
                ", seatNumber=" + seatNumber +
                ", isReserved=" + isReserved +
                ", roomName=" + roomName +
                '}';
    }
}
