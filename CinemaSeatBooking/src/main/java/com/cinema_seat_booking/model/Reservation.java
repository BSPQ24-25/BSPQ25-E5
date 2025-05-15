package com.cinema_seat_booking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToOne
	@JoinColumn(name = "screening_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Screening screening;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "seat_id")
	private Seat seat;

	@Enumerated(EnumType.STRING)
	private ReservationState reservationState;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "payment_id")
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
		if (seat != null) {
			seat.setReserved(true);
			seat.setReservation(this);
		}
	}

	public void setReservationState(ReservationState reservationState) {
		this.reservationState = reservationState;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
		if (payment != null) {
			payment.setReservation(this);
		}
	}

	// No-argument constructor (needed for JPA)
	public Reservation() {
		// This constructor is required by JPA
		this.reservationState = ReservationState.PENDING;
	}

	// Constructor
	public Reservation(User user, Screening screening, Payment payment, Seat seat) {
		this.user = user;
		this.screening = screening;
		this.reservationState = ReservationState.PENDING;
		this.setPayment(payment);
		this.setSeat(seat);
	}

	public Reservation(User user, Screening screening, Seat seat) {
		this.user = user;
		this.screening = screening;
		this.reservationState = ReservationState.PENDING;
		
		Payment newPayment = new Payment();
		this.payment = newPayment;
		newPayment.setReservation(this);
		
		this.setSeat(seat);
	}
}