package com.cinema_seat_booking.dto;

import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.model.User;

public class ReservationDTO {
    private Long reservationId;
    private User user;
    private ScreeningDTO screening;
    private SeatDTO seat;
    private String reservationState;

    // Constructor que mapea desde la entidad Reservation
    public ReservationDTO(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.user = reservation.getUser();
        this.screening = new ScreeningDTO(reservation.getScreening());
        this.seat = new SeatDTO(reservation.getSeat());
        if (reservation.getReservationState() != null) {
            this.reservationState = reservation.getReservationState().name();
        }
    }

    // Getters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ScreeningDTO getScreening() {
        return screening;
    }

    public void setScreening(ScreeningDTO screening) {
        this.screening = screening;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    public String getReservationState() {
        return reservationState;
    }

    public void setReservationState(String reservationState) {
        this.reservationState = reservationState;
    }
}
