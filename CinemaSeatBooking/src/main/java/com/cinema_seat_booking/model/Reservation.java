	
	package com.cinema_seat_booking.model;
	import jakarta.persistence.*;
	
	@Entity
	public class Reservation {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	
	    @ManyToOne
	    private User user;
	
	    @ManyToOne
	    private Screening screening;
	
	    @OneToOne
	    private Seat seat;
	
	    @Enumerated(EnumType.STRING)
	    private ReservationState reservationState;

	
	    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	    private Payment payment;
	    
	
	    // Getters
	    public Long getId() {
	        return id;
	    }
	
	    public User getUser() {
	        return user;
	    }
	
	    public Screening getScreening() {
	        return screening;
	    }
	
	    public Seat getSeat() {
	        return seat;
	    }
	
	    public ReservationState getReservationState() {
	        return reservationState;
	    }
	
	    public Payment getPayment() {
	        return payment;
	    }
	
	    // Setters
	    public void setId(Long id) {
	        this.id = id;
	    }
	
	    public void setUser(User user) {
	        this.user = user;
	    }
	
	    public void setScreening(Screening screening) {
	        this.screening = screening;
	    }
	
	    public void setSeat(Seat seat) {
	        this.seat = seat;
	    }
	
	    public void setReservationState(ReservationState reservationState) {
	        this.reservationState = reservationState;
	    }
	
	    public void setPayment(Payment payment) {
	        this.payment = payment;
	    }
	
	    
	    // No-argument constructor (needed for JPA)
	    public Reservation() {
	        // This constructor is required by JPA
	    }

		// Constructor
	    public Reservation(User user, Screening screening, Payment payment, Seat seat) {
	        super();
	        this.user = user;
	        this.screening = screening;
	        this.reservationState = ReservationState.PENDING;
	        this.payment = payment;
	        this.seat = seat;
	    }
	    
	    public Reservation(User user, Screening screening, Seat seat) {
	        this.user = user;
	        this.screening = screening;
	        this.reservationState = ReservationState.PENDING;
	        this.payment = new Payment();
	        this.seat = seat;
	    }
	}


