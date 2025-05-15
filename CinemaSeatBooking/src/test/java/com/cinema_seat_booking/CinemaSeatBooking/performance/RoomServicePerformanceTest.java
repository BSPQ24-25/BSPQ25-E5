package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.service.RoomService;

import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class RoomServicePerformanceTest {

    @Autowired
    private static RoomService roomService;

    private static Room testRoom;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/room-service-perf-report.html"))
        .build();

    @BeforeAll
    static void setup(@Autowired RoomService service) {
        roomService = service;
        // We'll use an existing room from the database instead of creating a new one
        testRoom = roomService.getAllRooms().get(0);
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
        Room updated = roomService.updateRoom(testRoom.getId(), "Updated_Room");
        assertNotNull(updated);
        assertTrue(updated.getName().startsWith("Updated_"));
        roomService.updateRoom(updated.getId(), "Room 1");
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