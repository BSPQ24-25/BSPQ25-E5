package com.cinema_seat_booking.dto;

import  com.cinema_seat_booking.dto.*;

import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.model.User;

public class ReservationDTO {
    private Long reservationId;
    private UserDTO user;
    private ScreeningDTO screening;
    private SeatDTO seat;
    private String reservationState;
 public ReservationDTO(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.user = new UserDTO(reservation.getUser()); // Map to UserDTO
        this.screening = new ScreeningDTO(reservation.getScreening()); // Map to ScreeningDTO
        this.seat = new SeatDTO(reservation.getSeat()); // Map to SeatDTO
        if (reservation.getReservationState() != null) {
            this.reservationState = reservation.getReservationState().name();
        }
    }
    // Constructor que mapea desde la entidad Reservation
   
     public ReservationDTO() {
    }


    // Getters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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
