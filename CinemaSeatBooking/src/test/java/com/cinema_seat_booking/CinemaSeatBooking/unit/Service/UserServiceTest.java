package com.cinema_seat_booking.CinemaSeatBooking.unit.Service;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.UserRepository;
import com.cinema_seat_booking.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        dto = new UserDTO();
        dto.setUsername("alice");
        dto.setPassword("pass");
        dto.setEmail("alice@example.com");
        user = new User("alice", "pass", "alice@example.com");
        user.setId(1L);
    }

    @Test
    void testRegisterUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.registerUser(dto);
        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        assertEquals(Role.CLIENT, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testGetUserByUsername_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(user);
        User result = userService.getUserByUsername("alice");
        assertEquals(user, result);
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("bob")).thenReturn(null);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.getUserByUsername("bob"));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(user);
        boolean deleted = userService.deleteUser("alice", "pass");
        assertTrue(deleted);
        verify(userRepository).delete(user);
    }

    @Test
    void testDeleteUser_Failure() {
        when(userRepository.findByUsername("alice")).thenReturn(user);
        boolean deleted = userService.deleteUser("alice", "wrong");
        assertFalse(deleted);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testUpdateUserProfile_Success() {
        when(userRepository.findByUsername("alice")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        dto.setUsername("alice");
        dto.setEmail("new@example.com");
        dto.setPassword("newpass");
        User updated = userService.updateUserProfile(dto);

        assertEquals("new@example.com", updated.getEmail());
        assertEquals("newpass", updated.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserProfile_NotFound() {
        when(userRepository.findByUsername("bob")).thenReturn(null);
        UserDTO missing = new UserDTO();
        missing.setUsername("bob");
        missing.setEmail("b@example.com");
        missing.setPassword("pass");

        assertThrows(RuntimeException.class, () -> userService.updateUserProfile(missing));
    }
}