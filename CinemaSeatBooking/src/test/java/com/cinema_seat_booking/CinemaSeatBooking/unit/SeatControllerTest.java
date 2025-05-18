package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.service.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = com.cinema_seat_booking.controller.CinemaSeatBookingApplication.class)
@AutoConfigureMockMvc
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSeats() throws Exception {
        Room room = new Room();
        room.setName("Room A");

        Seat seat1 = new Seat();
        seat1.setId(1L);
        seat1.setSeatNumber(1);
        seat1.setReserved(false);
        seat1.setRoom(room);

        Seat seat2 = new Seat();
        seat2.setId(2L);
        seat2.setSeatNumber(2);
        seat2.setReserved(true);
        seat2.setRoom(room);

        when(seatService.getAllSeats()).thenReturn(Arrays.asList(seat1, seat2));

        mockMvc.perform(get("/api/seats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].seatNumber").value(1))
                .andExpect(jsonPath("$[0].reserved").value(false))
                .andExpect(jsonPath("$[0].roomName").value("Room A"));
    }

    @Test
    void testGetSeatById_Found() throws Exception {
        Room room = new Room();
        room.setName("Room A");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(1);
        seat.setReserved(false);
        seat.setRoom(room);

        when(seatService.getSeatById(1L)).thenReturn(Optional.of(seat));

        mockMvc.perform(get("/api/seats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(1))
                .andExpect(jsonPath("$.reserved").value(false))
                .andExpect(jsonPath("$.roomName").value("Room A"));
    }

    @Test
    void testGetSeatById_NotFound() throws Exception {
        when(seatService.getSeatById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/seats/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSeat_Success() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Room A");

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(10);
        seat.setRoom(room);

        when(seatService.createSeat(10, 1L)).thenReturn(seat);

        mockMvc.perform(post("/api/seats")
                .param("number", "10")
                .param("roomId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value(10));
    }

    @Test
    void testUpdateSeat_Success() throws Exception {
        Room room = new Room();
        room.setId(1L);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(5);
        seat.setRoom(room);

        when(seatService.updateSeat(1L, 5, 1L)).thenReturn(seat);

        mockMvc.perform(put("/api/seats/1")
                .param("number", "5")
                .param("roomId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(5));
    }

    @Test
    void testUpdateSeat_NotFound() throws Exception {
        when(seatService.updateSeat(99L, 5, 1L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/seats/99")
                .param("number", "5")
                .param("roomId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSeat_Success() throws Exception {
        when(seatService.deleteSeat(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/seats/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSeat_NotFound() throws Exception {
        when(seatService.deleteSeat(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/seats/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAvailableSeats() throws Exception {
        Room room = new Room();
        room.setId(1L);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(3);
        seat.setReserved(false);
        seat.setRoom(room);

        when(seatService.getAvailableSeats(1L)).thenReturn(Collections.singletonList(seat));

        mockMvc.perform(get("/api/seats/available")
                .param("roomId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value(3));
    }

    @Test
    void testReserveSeat_Success() throws Exception {
        doNothing().when(seatService).reserveSeat(1L);

        mockMvc.perform(put("/api/seats/1/reserve"))
                .andExpect(status().isOk());
    }

    @Test
    void testReserveSeat_Failure() throws Exception {
        doThrow(new RuntimeException()).when(seatService).reserveSeat(99L);

        mockMvc.perform(put("/api/seats/99/reserve"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCancelReservation_Success() throws Exception {
        doNothing().when(seatService).cancelSeatReservation(1L);

        mockMvc.perform(put("/api/seats/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelReservation_Failure() throws Exception {
        doThrow(new RuntimeException()).when(seatService).cancelSeatReservation(99L);

        mockMvc.perform(put("/api/seats/99/cancel"))
                .andExpect(status().isBadRequest());
    }
}
