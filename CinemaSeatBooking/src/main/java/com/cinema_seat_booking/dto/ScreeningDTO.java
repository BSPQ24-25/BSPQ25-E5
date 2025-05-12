package com.cinema_seat_booking.dto;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Screening;

public class ScreeningDTO {
    private Long id;
    private Movie movie;
    private String date;
    private String location;
    private RoomDTO room;
    public ScreeningDTO() {
    }

    public ScreeningDTO(Long id, Movie movie, String date, String location, Room room) {
        this.id = id;
        this.movie = movie;
        this.date = date;
        this.location = location;
        this.room = new RoomDTO(room);
    }

    public ScreeningDTO(Screening screening) {
        this.id = screening.getId();
        this.movie = screening.getMovie();
        this.date = screening.getDate();
        this.location = screening.getLocation();
        this.room = new RoomDTO(screening.getRoom());
    }

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

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

}
