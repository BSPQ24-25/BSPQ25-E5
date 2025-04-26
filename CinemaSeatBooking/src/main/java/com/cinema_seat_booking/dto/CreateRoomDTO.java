package com.cinema_seat_booking.dto;

public class CreateRoomDTO {

    private String name;

    public CreateRoomDTO() {
    }

    public CreateRoomDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CreateRoomDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}
