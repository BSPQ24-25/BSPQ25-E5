package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.service.RoomService;

import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class RoomServicePerformanceTest {

    @Autowired
    private RoomService roomService;

    private Room testRoom;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/room-service-perf-report.html"))
        .build();

    @BeforeEach
    void setup() {
        testRoom = roomService.createRoomWithSeats("Room_" + UUID.randomUUID());
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 7000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 15, percentiles = "95:300ms", allowedErrorPercentage = 1.0f)
    public void testCreateRoomWithSeatsPerformance() {
        Room room = roomService.createRoomWithSeats("Room_" + UUID.randomUUID());
        assertNotNull(room);
        assertEquals(20, room.getSeats().size());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:300ms", allowedErrorPercentage = 1.0f)
    public void testGetRoomByIdPerformance() {
        Room result = roomService.getRoomById(testRoom.getId()).orElse(null);
        assertNotNull(result);
        assertEquals(testRoom.getId(), result.getId());
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 8, percentiles = "95:400ms", allowedErrorPercentage = 2.0f)
    public void testUpdateRoomPerformance() {
        Room updated = roomService.updateRoom(testRoom.getId(), "Updated_" + testRoom.getName());
        assertNotNull(updated);
        assertTrue(updated.getName().startsWith("Updated_"));
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 8, percentiles = "95:400ms", allowedErrorPercentage = 1.0f)
    public void testDeleteRoomPerformance() {
        Room room = roomService.createRoomWithSeats("RoomToDelete_" + UUID.randomUUID());
        roomService.deleteRoom(room.getId());
        assertFalse(roomService.getRoomById(room.getId()).isPresent());
    }

    // Simulate a failure case: updating a non-existent room
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 3000, warmUpMs = 500)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:400ms", allowedErrorPercentage = 0.0f)
    public void testUpdateRoomFailureCasePerformance() {
        assertThrows(NoSuchElementException.class, () -> {
            roomService.updateRoom(999999L, "ShouldFail");
        });
    }
}

