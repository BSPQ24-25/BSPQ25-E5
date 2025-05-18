package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

/**
 * @class Movie
 * @brief Entity class representing a movie.
 *
 * @details
 * The {@code Movie} class maps to the "movie" table in the database.
 * It stores details about a movie such as title, duration, genre, cast,
 * and its related screenings.
 *
 * @author BSPQ25-E5
 * @version 1.0
 * @since 2025-05-19
 */
@Entity
public class Movie {
    /**
     * @brief Unique identifier for the movie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief Title of the movie.
     *
     * @details
     * This field is unique and cannot be null.
     */
    @Column(nullable = false, unique = true)
    private String title;

    /**
     * @brief Duration of the movie in minutes.
     */
    @Column(nullable = false)
    private int duration;

    /**
     * @brief Genre of the movie.
     */
    @Column(nullable = false)
    private String genre;

    /**
     * @brief Cast of the movie.
     *
     * @details
     * Stored in the database column named "movie_cast".
     */
    @Column(nullable = false, name = "movie_cast")
    private String cast;

    /**
     * @brief List of screenings associated with this movie.
     *
     * @details
     * One-to-many relationship; a movie can have multiple screenings.
     * Screenings are ignored in JSON serialization to avoid circular references.
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Screening> screenings;

    /**
     * @brief Default constructor required by JPA.
     *
     * @details
     * Initializes the screenings list.
     */
    public Movie() {
        this.screenings = new ArrayList<>();
    }

    /**
     * @brief Constructs a movie with all fields.
     *
     * @param title the movie title
     * @param duration the movie duration in minutes
     * @param genre the movie genre
     * @param cast the movie cast
     * @param screenings the list of screenings for this movie
     */
    public Movie(String title, int duration, String genre, String cast, List<Screening> screenings) {
        super();
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.cast = cast;
        this.screenings = screenings != null ? screenings : new ArrayList<>();
    }

    /**
     * @brief Constructs a movie without screenings.
     *
     * @param title the movie title
     * @param duration the movie duration in minutes
     * @param genre the movie genre
     * @param cast the movie cast
     */
    public Movie(String title, int duration, String genre, String cast) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.cast = cast;
        this.screenings = new ArrayList<>();
    }

    /**
     * @brief Gets the ID of the movie.
     *
     * @return the movie ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Sets the ID of the movie.
     *
     * @param id the movie ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Gets the title of the movie.
     *
     * @return the movie title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @brief Sets the title of the movie.
     *
     * @param title the movie title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @brief Gets the duration of the movie.
     *
     * @return the movie duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @brief Sets the duration of the movie.
     *
     * @param duration the movie duration in minutes to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @brief Gets the genre of the movie.
     *
     * @return the movie genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @brief Sets the genre of the movie.
     *
     * @param genre the movie genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @brief Gets the cast of the movie.
     *
     * @return the movie cast
     */
    public String getCast() {
        return cast;
    }

    /**
     * @brief Sets the cast of the movie.
     *
     * @param cast the movie cast to set
     */
    public void setCast(String cast) {
        this.cast = cast;
    }

    /**
     * @brief Gets the list of screenings for the movie.
     *
     * @return the list of screenings
     */
    public List<Screening> getScreenings() {
        return screenings;
    }

    /**
     * @brief Sets the list of screenings for the movie.
     *
     * @param screenings the list of screenings to set
     */
    public void setScreenings(List<Screening> screenings) {
        this.screenings = screenings;
    }

    /**
     * @brief Adds a screening to this movie.
     *
     * @param screening the screening to add
     *
     * @details
     * Initializes the screenings list if null, then adds the screening
     * and sets the movie of the screening to this instance.
     */
    public void addScreening(Screening screening) {
        if (this.screenings == null) {
            this.screenings = new ArrayList<>();
        }
        this.screenings.add(screening);
        screening.setMovie(this);
    }
}
