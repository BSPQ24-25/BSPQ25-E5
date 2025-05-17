package com.cinema_seat_booking.dto;

public class CreateRoomDTO {

    private String name;
    private int seatCount;

    public CreateRoomDTO() {
    }

    public CreateRoomDTO(String name, int seatCount) {
        this.name = name;
        this.seatCount = seatCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    @Override
    public String toString() {
        return "CreateRoomDTO{" +
               "name='" + name + '\'' +
               ", seatCount=" + seatCount +
               '}';
    }
}
