package com.cinema_seat_booking.CinemaSeatBooking.unit;

import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.SeatRepository;
import com.cinema_seat_booking.service.SeatService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    private Seat reservedSeat;

    @BeforeEach
    void setUp() {
        reservedSeat = new Seat();
        reservedSeat.setId(1L);
        reservedSeat.setReserved(true);
    }

    @Test
    void testCancelSeatReservation_Success() throws Exception {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(reservedSeat));
        seatService.cancelSeatReservation(1L);
        assertFalse(reservedSeat.isReserved());
        verify(seatRepository).save(reservedSeat);
    }

    @Test
    void testCancelSeatReservation_NotFound() {
        when(seatRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> seatService.cancelSeatReservation(2L));
        assertEquals("Seat not found", ex.getMessage());
    }

    @Test
    void testCancelSeatReservation_NotReserved() {
        Seat freeSeat = new Seat(); freeSeat.setId(3L); freeSeat.setReserved(false);
        when(seatRepository.findById(3L)).thenReturn(Optional.of(freeSeat));
        Exception ex = assertThrows(Exception.class, () -> seatService.cancelSeatReservation(3L));
        assertEquals("Seat is not reserved", ex.getMessage());
    }
}