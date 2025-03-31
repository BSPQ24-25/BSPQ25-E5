package com.cinema_seat_booking.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String title;
    
    @Column(nullable = false)
    private int duration;
    
    @Column(nullable = false)
    private String genre;
    
    @Column(nullable = false, name = "movie_cast")
    private String cast;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Screening> screenings;

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
	
    // No-argument constructor (needed for JPA)
    public Movie() {
        // This constructor is required by JPA
    }

	public Movie(String title, int duration, String genre, String cast, List<Screening> screenings) {
		super();
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.cast = cast;
		this.screenings = screenings;
	}
    
	public Movie(String title, int duration, String genre, String cast) {
		this.title = title;
		this.duration = duration;
		this.genre = genre;
		this.cast = cast;
		this.screenings = new ArrayList<>();
	}
}