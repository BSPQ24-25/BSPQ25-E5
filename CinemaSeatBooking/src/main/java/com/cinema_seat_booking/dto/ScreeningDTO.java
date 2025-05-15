package com.cinema_seat_booking.dto;

import java.util.ArrayList;
import java.util.List;

import com.cinema_seat_booking.dto.*;
import com.cinema_seat_booking.model.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScreeningDTO {
    private Long id;
    private Movie movie;
    private String date;
    private String location;
    private RoomDTO room;
public ScreeningDTO() {
    this.date = "defaultDate";
    this.movie = createDefaultMovie(); // Assign a default Movie object
    this.location = "defaultLocation";
    this.room = createDefaultRoomDTO(); // Assign a default RoomDTO object
}

// Helper method to create a default Movie object
private Movie createDefaultMovie() {
    Movie defaultMovie = new Movie();
    defaultMovie.setId(0L); // Default ID
    defaultMovie.setTitle("Default Movie"); // Default title
    defaultMovie.setDuration(120); // Default duration in minutes
    return defaultMovie;
}

// Helper method to create a default RoomDTO object
private RoomDTO createDefaultRoomDTO() {
    Room room = new Room("defroom");
    RoomDTO defaultRoom=new RoomDTO(room);
    defaultRoom.setId(0L); // Default ID
     // Empty seat list
    return defaultRoom;
}
@JsonCreator
public ScreeningDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("movie") Movie movie,
        @JsonProperty("date") String date,
        @JsonProperty("location") String location,
        @JsonProperty("room") RoomDTO room) {
    this.id = id;
    this.movie = movie != null ? movie : createDefaultMovie();
    this.date = date != null ? date : "defaultDate";
    this.location = location != null ? location : "defaultLocation";
    this.room = room != null ? room : createDefaultRoomDTO();
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
