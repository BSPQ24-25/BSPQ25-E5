package com.cinema_seat_booking.model;

import java.time.LocalDateTime;
import java.util.List;

import com.cinema_seat_booking.model.User;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @OneToMany // at least one seat in a RES and to many , ONE client? (i think as only one
               // needed to reserve for all?)
    @JoinTable(name = "reservation_seats", // junction table to link seats to a reservation not sure if this is a good
                                           // idea (might need to change )
            joinColumns = @JoinColumn(name = "reservation_id"), inverseJoinColumns = @JoinColumn(name = "seat_id"))
    private List<Seat> seats;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    @Column(nullable = false)
    private String paymentMethod;

    // cnstrctr
    public Reservation() {
    }

    public Reservation(User user, Screening screening, List<Seat> seats, String paymentMethod) {
        this.user = user;
        this.screening = screening;
        this.seats = seats;
        this.paymentMethod = paymentMethod;
        this.date = LocalDateTime.now();
        this.reservationState = ReservationState.PENDING;
    }
    //////
    // public void confirmReservation() {
    // if (!seat.isReserved()) {
    // seat.reserve();
    // this.reservationState = ReservationState.PAYED;
    // } else {
    // System.out.println("Seat already reserved.");
    // }
    // }

    //////////////////////////////////////////
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ReservationState getReservationState() {
        return reservationState;
    }

    public void setReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Enum for Reservation State i dont know if its what we want ?? :()
    public enum ReservationState {
        PAYED,
        PENDING
    }
}
