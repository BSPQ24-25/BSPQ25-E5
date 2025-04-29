package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.Reservation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    void TestGettersAndSetters() {
        User user = new User();
        
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("testuser@example.com");
        user.setRole(Role.CLIENT);

        List<Reservation> reservations = new ArrayList<>();
        user.setReservations(reservations);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals(Role.CLIENT, user.getRole());
        assertEquals(reservations, user.getReservations());
    }

    @Test
    void TestAddAndRemoveReservation() {
        User user = new User("testuser", "password123", "testuser@example.com", Role.CLIENT, new ArrayList<>());

        Reservation reservation = new Reservation();
        user.addReservation(reservation);

        assertEquals(1, user.getReservations().size());
        assertEquals(user, reservation.getUser());

        user.removeReservation(reservation);

        assertEquals(0, user.getReservations().size());
        assertEquals(null, reservation.getUser());
    }
}
