package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.service.SeatService;

import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class SeatServicePerformanceTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private RoomRepository roomRepository;

    private Room testRoom;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/seat-service-perf-report.html"))
        .build();

    @BeforeEach
    void setup() {
        testRoom = new Room("Room_" + UUID.randomUUID());
        roomRepository.save(testRoom);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:400ms", allowedErrorPercentage = 1.0f)
    public void testCreateSeatPerformance() {
        int number = (int) (Math.random() * 1000);
        Seat seat = seatService.createSeat(number, testRoom.getId());
        assertNotNull(seat);
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:300ms", allowedErrorPercentage = 1.0f)
    public void testGetSeatByIdPerformance() {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        Seat result = seatService.getSeatById(seat.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(seat.getId(), result.getId());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 1.0f)
    public void testUpdateSeatPerformance() {
        Seat seat = seatService.createSeat(101, testRoom.getId());
        Seat updated = seatService.updateSeat(seat.getId(), 202, testRoom.getId());
        assertNotNull(updated);
        assertEquals(202, updated.getSeatNumber());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 2.0f)
    public void testReserveSeatPerformance() throws Exception {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        seatService.reserveSeat(seat.getId());
        Seat updated = seatService.getSeatById(seat.getId()).orElse(null);
        assertNotNull(updated);
        assertTrue(updated.isReserved());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 2.0f)
    public void testCancelSeatReservationPerformance() throws Exception {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        seatService.reserveSeat(seat.getId());
        seatService.cancelSeatReservation(seat.getId());
        Seat updated = seatService.getSeatById(seat.getId()).orElse(null);
        assertNotNull(updated);
        assertFalse(updated.isReserved());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 1.0f)
    public void testGetAvailableSeatsPerformance() {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        List<Seat> available = seatService.getAvailableSeats(testRoom.getId());
        assertNotNull(available);
        assertTrue(available.stream().anyMatch(s -> s.getId().equals(seat.getId())));
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 0.0f)
    public void testDeleteSeatPerformance() {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        boolean deleted = seatService.deleteSeat(seat.getId());
        assertTrue(deleted);
    }

    // Failure test — reserve a seat that's already reserved
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 500)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:500ms", allowedErrorPercentage = 0.0f)
    public void testReserveAlreadyReservedSeatPerformance() throws Exception {
        Seat seat = seatService.createSeat((int) (Math.random() * 1000), testRoom.getId());
        seatService.reserveSeat(seat.getId());

        Exception ex = assertThrows(Exception.class, () -> seatService.reserveSeat(seat.getId()));
        assertTrue(ex.getMessage().contains("already reserved"));
    }
}

