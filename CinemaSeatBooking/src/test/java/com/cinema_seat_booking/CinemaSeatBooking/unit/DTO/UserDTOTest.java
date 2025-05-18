package com.cinema_seat_booking.CinemaSeatBooking.unit.DTO;

import com.cinema_seat_booking.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("securePassword123");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void testUsernameGetterSetter() {
        assertEquals("testUser", userDTO.getUsername());

        userDTO.setUsername("newUser");
        assertEquals("newUser", userDTO.getUsername());
    }

    @Test
    void testPasswordGetterSetter() {
        assertEquals("securePassword123", userDTO.getPassword());

        userDTO.setPassword("newPassword");
        assertEquals("newPassword", userDTO.getPassword());
    }

    @Test
    void testEmailGetterSetter() {
        assertEquals("test@example.com", userDTO.getEmail());

        userDTO.setEmail("new@example.com");
        assertEquals("new@example.com", userDTO.getEmail());
    }
}