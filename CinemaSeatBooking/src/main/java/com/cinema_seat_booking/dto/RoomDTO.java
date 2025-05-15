package com.cinema_seat_booking.dto;

import java.util.ArrayList;
import java.util.List;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;

public class RoomDTO {
    private Long id;
    private String name;
    private List<SeatDTO> seats;

    public RoomDTO(Long id, String name, List<Seat> seats) {
        this.id = id;
        this.name = name;
        List<SeatDTO> seatDTOs = new ArrayList<>();
        for (Seat seat : seats) {
            seatDTOs.add(new SeatDTO(seat));
        }
        this.seats = seatDTOs;
    }

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        List<SeatDTO> seatDTOs = new ArrayList<>();
        for (Seat seat : room.getSeats()) {
            seatDTOs.add(new SeatDTO(seat));
        }
        this.seats = seatDTOs;
    }

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

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }
  public Room toEntity() {
        Room room = new Room();
        room.setId(this.id);
        room.setName(this.name);
        if (this.seats != null) {
            List<Seat> seatEntities = new ArrayList<>();
            for (SeatDTO seatDTO : this.seats) {
                seatEntities.add(seatDTO.toEntity()); // Assuming SeatDTO has a toEntity() method
            }
            room.setSeats(seatEntities);
        }
        return room;
    }
}
