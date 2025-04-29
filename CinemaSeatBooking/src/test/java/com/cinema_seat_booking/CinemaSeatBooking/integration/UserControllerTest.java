package com.cinema_seat_booking.CinemaSeatBooking.integration;

import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john");
        userDTO.setPassword("pass");

        User user = new User();
        user.setUsername("john");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void testViewProfile_UserExists() throws Exception {
        User user = new User();
        user.setUsername("john");

        when(userService.getUserByUsername("john")).thenReturn(user);

        mockMvc.perform(get("/api/users/profile")
                .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService, times(1)).getUserByUsername("john");
    }

    @Test
    void testViewProfile_UserNotFound() throws Exception {
        when(userService.getUserByUsername("nonexistent")).thenReturn(null);

        mockMvc.perform(get("/api/users/profile")
                .param("username", "nonexistent"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserByUsername("nonexistent");
    }

    @Test
    void testUpdateProfile_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john");

        User updatedUser = new User();
        updatedUser.setUsername("john");

        when(userService.updateUserProfile(any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));

        verify(userService, times(1)).updateUserProfile(any(UserDTO.class));
    }

    @Test
    void testUpdateProfile_Failure() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("invalid");

        when(userService.updateUserProfile(any(UserDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).updateUserProfile(any(UserDTO.class));
    }

    @Test
    void testDeleteProfile_Success() throws Exception {
        when(userService.deleteUser("john", "pass")).thenReturn(true);

        mockMvc.perform(delete("/api/users/profile")
                .param("username", "john")
                .param("password", "pass"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser("john", "pass");
    }

    @Test
    void testDeleteProfile_Failure() throws Exception {
        when(userService.deleteUser("john", "wrong")).thenReturn(false);

        mockMvc.perform(delete("/api/users/profile")
                .param("username", "john")
                .param("password", "wrong"))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).deleteUser("john", "wrong");
    }
}
