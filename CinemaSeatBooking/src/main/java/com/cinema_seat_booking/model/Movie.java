package com.cinema_seat_booking.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String cast;

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Screening> screenings;

    public Movie() {
    }

    public Movie(String title, int duration, String genre, String cast, List<Screening> screenings) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.cast = cast;
        this.screenings = screenings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                ", cast='" + cast + '\'' +
                '}';
    }
}
