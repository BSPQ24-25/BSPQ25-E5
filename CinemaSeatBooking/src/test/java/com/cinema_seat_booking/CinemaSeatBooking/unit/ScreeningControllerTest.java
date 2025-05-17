package com.cinema_seat_booking.CinemaSeatBooking.unit;


import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.service.ScreeningService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.cinema_seat_booking.controller.CinemaSeatBookingApplication.class)
@AutoConfigureMockMvc
class ScreeningControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningService screeningService; 

    @Test
    void testGetAllScreenings() throws Exception {
        // Arrange: Mock the service layer
        Movie movie = new Movie();
        movie.setTitle("Movie A");

        Room room = new Room();
        room.setName("Room 1");

        Screening screening1 = new Screening(movie, "2025-04-10 18:00", "Location A", room);
        Screening screening2 = new Screening(movie, "2025-04-10 20:00", "Location B", room);
        List<Screening> screenings = Arrays.asList(screening1, screening2);

        when(screeningService.getAllScreenings()).thenReturn(screenings);

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/api/screenings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].date").value("2025-04-10 18:00"))
                .andExpect(jsonPath("$[0].location").value("Location A"))
                .andExpect(jsonPath("$[1].date").value("2025-04-10 20:00"))
                .andExpect(jsonPath("$[1].location").value("Location B"));

        // Verify the service was called
        verify(screeningService, times(1)).getAllScreenings();
    }

    @Test
    void testGetScreeningById() throws Exception {
        // Arrange: Mock the service layer
        Movie movie = new Movie();
        movie.setTitle("Movie A");

        Room room = new Room();
        room.setName("Room 1");

        Screening screening = new Screening(movie, "2025-04-10 18:00", "Location A", room);
        screening.setId(1L);

        // Wrap the Screening object in an Optional
        when(screeningService.getScreeningById(1L)).thenReturn(Optional.of(screening));

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/api/screenings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2025-04-10 18:00"))
                .andExpect(jsonPath("$.location").value("Location A"))
                .andExpect(jsonPath("$.room.name").value("Room 1"));

        // Verify the service was called
        verify(screeningService, times(1)).getScreeningById(1L);
    }

    @Test
    void testGetScreeningById_NotFound() throws Exception {
        // Arrange: Mock the service layer to return an empty Optional
        when(screeningService.getScreeningById(999L)).thenReturn(Optional.empty());

        // Act & Assert: Perform GET request and verify 404 response
        mockMvc.perform(get("/api/screenings/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verify the service was called
        verify(screeningService, times(1)).getScreeningById(999L);
    }

    @Test
    void testGetAllScreenings_EmptyList() throws Exception {
        // Arrange: Mock the service layer to return an empty list
        when(screeningService.getAllScreenings()).thenReturn(Collections.emptyList());

        // Act & Assert: Perform GET request and verify empty response
        mockMvc.perform(get("/api/screenings")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        // Verify the service was called
        verify(screeningService, times(1)).getAllScreenings();
    }
}