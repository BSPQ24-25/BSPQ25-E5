package com.cinema_seat_booking.dto;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.model.Screening;

public class ScreeningDTO {
    private Long id;
    private Movie movie;
    private String date;
    private String location;

    public ScreeningDTO(Screening screening) {
        this.id = screening.getId();
        this.movie = screening.getMovie();
        this.date = screening.getDate();
        this.location = screening.getLocation();
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

}
