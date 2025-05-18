package com.cinema_seat_booking.CinemaSeatBooking.unit.Model;

import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.Reservation;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testConstructorWithAllFields() {
        List<Reservation> reservations = new ArrayList<>();
        User user = new User("alice", "secure123", "alice@example.com", Role.ADMIN, reservations);

        assertEquals("alice", user.getUsername());
        assertEquals("secure123", user.getPassword());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(reservations, user.getReservations());
    }

    @Test
    void testConstructorWithoutReservations() {
        User user = new User("bob", "pass456", "bob@example.com", Role.CLIENT);

        assertEquals("bob", user.getUsername());
        assertEquals("pass456", user.getPassword());
        assertEquals("bob@example.com", user.getEmail());
        assertEquals(Role.CLIENT, user.getRole());
        assertNull(user.getReservations());
    }

    @Test
    void testConstructorWithDefaultRoleClient() {
        User user = new User("charlie", "abc123", "charlie@example.com");

        assertEquals("charlie", user.getUsername());
        assertEquals("abc123", user.getPassword());
        assertEquals("charlie@example.com", user.getEmail());
        assertEquals(Role.CLIENT, user.getRole());
        assertNull(user.getReservations());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        List<Reservation> reservations = new ArrayList<>();
        user.setId(100L);
        user.setUsername("dave");
        user.setPassword("mypassword");
        user.setEmail("dave@example.com");
        user.setRole(Role.ADMIN);
        user.setReservations(reservations);

        assertEquals(100L, user.getId());
        assertEquals("dave", user.getUsername());
        assertEquals("mypassword", user.getPassword());
        assertEquals("dave@example.com", user.getEmail());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(reservations, user.getReservations());
    }

    @Test
    void testAddReservation() {
        User user = new User("eve", "pwd", "eve@example.com");
        Reservation reservation = new Reservation();

        user.addReservation(reservation);

        assertNotNull(user.getReservations());
        assertEquals(1, user.getReservations().size());
        assertEquals(reservation, user.getReservations().get(0));
        assertEquals(user, reservation.getUser());
    }

    @Test
    void testRemoveReservation() {
        User user = new User("frank", "pwd", "frank@example.com");
        Reservation reservation = new Reservation();
        user.addReservation(reservation);

        user.removeReservation(reservation);

        assertTrue(user.getReservations().isEmpty());
        assertNull(reservation.getUser());
    }
}
