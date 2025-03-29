package com.cinema_seat_booking.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "screenings")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String location;

    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "screening", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Screening() {
    }

    public Screening(Movie movie, String date, String location) {
        this.movie = movie;
        this.date = date;
        this.location = location;
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
