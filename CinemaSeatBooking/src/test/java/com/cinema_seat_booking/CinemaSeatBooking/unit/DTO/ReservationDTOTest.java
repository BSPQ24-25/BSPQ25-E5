package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.ReservationDTO;
import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.dto.SeatDTO;
import com.cinema_seat_booking.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationDTOTest {

    private Reservation reservation;
    private User user;
    private Screening screening;
    private Seat seat;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Room room = new Room();
        room.setId(1L);
        room.setName("Room A");

        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(10);
        seat.setRoom(room);

        screening = new Screening();
        screening.setId(1L);
        screening.setRoom(room);
        screening.setDate("2025-06-01");
        screening.setLocation("Main Hall");

        reservation = new Reservation();
        reservation.setId(100L);
        reservation.setUser(user);
        reservation.setSeat(seat);
        reservation.setScreening(screening);
        reservation.setReservationState(ReservationState.PAID);
    }

    @Test
    void testReservationDTOConstructorFromEntity() {
        ReservationDTO dto = new ReservationDTO(reservation);

        assertEquals(100L, dto.getReservationId());
        assertEquals(user, dto.getUser());
        assertNotNull(dto.getScreening());
        assertNotNull(dto.getSeat());
        assertEquals(ReservationState.PAID.toString(), dto.getReservationState());
    }

    @Test
    void testSettersAndGetters() {
        ReservationDTO dto = new ReservationDTO(reservation);

        dto.setReservationId(200L);
        assertEquals(200L, dto.getReservationId());

        User newUser = new User();
        newUser.setId(2L);
        dto.setUser(newUser);
        assertEquals(2L, dto.getUser().getId());

        ScreeningDTO screeningDTO = new ScreeningDTO(screening);
        dto.setScreening(screeningDTO);
        assertEquals(screening.getId(), dto.getScreening().getId());

        SeatDTO seatDTO = new SeatDTO(seat);
        dto.setSeat(seatDTO);
        assertEquals(seat.getId(), dto.getSeat().getId());

        dto.setReservationState(ReservationState.PENDING.toString());
        assertEquals(ReservationState.PENDING.toString(), dto.getReservationState());
    }
}
