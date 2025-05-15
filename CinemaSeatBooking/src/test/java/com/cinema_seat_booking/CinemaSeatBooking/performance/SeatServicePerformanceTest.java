package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.service.SeatService;
import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance tests for SeatService that don't use Mockito
 * and don't create new records in the database.
 * 
 * All test methods are wrapped in @Transactional to ensure
 * any changes are rolled back after test execution.
 */
@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class SeatServicePerformanceTest {

    @Autowired
    private SeatService seatService;
    
    // Test data
    private static Room testRoom;
    private static Seat testSeat;
    private static Long testSeatId;
    private static Long testRoomId;
    
    // Configuration for JUnitPerf reports
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/seat-service-perf-report.html"))
            .build();

    /**
     * Set up test data - this is just preparing variables
     * that will be used for test conditions, not actually 
     * persisting anything to the database yet.
     */
    @BeforeEach
    void setup() {
        // Set these to IDs that you know exist in your test database
        // These should be pre-existing records in your test database
        testRoomId = 1L; // Use an existing room ID from your test database
        testSeatId = 1L; // Use an existing seat ID from your test database
    }

    /**
     * Test the performance of getSeatById operation
     */
    @Test
    @Transactional(readOnly = true)
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
    public void testGetSeatByIdPerformance() {
        // Simply test the read performance without modifying data
        Optional<Seat> result = seatService.getSeatById(testSeatId);
        assertTrue(result.isPresent(), "Test seat should exist");
    }

    /**
     * Test the performance of getAvailableSeats operation
     */
    @Test
    @Transactional(readOnly = true)
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
    public void testGetAvailableSeatsPerformance() {
        // Test read performance for available seats query
        List<Seat> available = seatService.getAvailableSeats(testRoomId);
        assertNotNull(available, "Available seats list should not be null");
    }

    /**
     * Test the performance of reserving and canceling seat operations
     * within a transaction (changes will be rolled back)
     */
    @Test
    @Transactional
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
    public void testReserveAndCancelSeatPerformance() throws Exception {
        // Get an available seat from the database
        List<Seat> availableSeats = seatService.getAvailableSeats(testRoomId);
        
        // Skip test if no available seats
        if (availableSeats.isEmpty()) {
            return;
        }
        
        // Get the first available seat
        Seat availableSeat = availableSeats.get(0);
        Long seatId = availableSeat.getId();
        
        try {
            // Test reservation performance
            seatService.reserveSeat(seatId);
            
            // Verify seat is now reserved
            Optional<Seat> reservedSeat = seatService.getSeatById(seatId);
            assertTrue(reservedSeat.isPresent() && reservedSeat.get().isReserved(),
                    "Seat should be marked as reserved");
            
            // Test cancellation performance
            seatService.cancelSeatReservation(seatId);
            
            // Verify seat is no longer reserved
            Optional<Seat> canceledSeat = seatService.getSeatById(seatId);
            assertTrue(canceledSeat.isPresent() && !canceledSeat.get().isReserved(),
                    "Seat should no longer be reserved");
        } catch (Exception e) {
            // If the test fails for any reason, make sure we reset the seat state
            try {
                seatService.cancelSeatReservation(seatId);
            } catch (Exception ignored) {
                // Ignore exceptions during cleanup
            }
            throw e;
        }
    }

    /**
     * Test exception performance when trying to reserve an already reserved seat
     * This test will only run if we have at least one available seat to first reserve
     */
    @Test
    @Transactional
    @JUnitPerfTest(threads = 3, durationMs = 4000, warmUpMs = 500)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
    public void testReserveAlreadyReservedSeatPerformance() {
        // Get an available seat from the database
        List<Seat> availableSeats = seatService.getAvailableSeats(testRoomId);
        
        // Skip test if no available seats
        if (availableSeats.isEmpty()) {
            return;
        }
        
        // Get the first available seat
        Seat availableSeat = availableSeats.get(0);
        Long seatId = availableSeat.getId();
        
        try {
            // First reserve the seat
            seatService.reserveSeat(seatId);
            
            // Now try to reserve it again and expect an exception
            Exception ex = assertThrows(Exception.class, () -> seatService.reserveSeat(seatId));
            assertTrue(ex.getMessage().contains("already reserved"));
            
        } catch (Exception e) {
            // Test setup failure
            fail("Test setup failed: " + e.getMessage());
        } finally {
            // Always make sure we clean up by canceling the reservation
            try {
                seatService.cancelSeatReservation(seatId);
            } catch (Exception ignored) {
                // Ignore exceptions during cleanup
            }
        }
    }

    /**
     * Test performance of trying to cancel a seat that isn't reserved
     */
    @Test
    @Transactional
    @JUnitPerfTest(threads = 3, durationMs = 4000, warmUpMs = 500)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
    public void testCancelNonReservedSeatPerformance() {
        // Get an available seat from the database
        List<Seat> availableSeats = seatService.getAvailableSeats(testRoomId);
        
        // Skip test if no available seats
        if (availableSeats.isEmpty()) {
            return;
        }
        
        // Get the first available seat (which is not reserved)
        Seat availableSeat = availableSeats.get(0);
        Long seatId = availableSeat.getId();
        
        // Try to cancel a non-reserved seat
        Exception ex = assertThrows(Exception.class, () -> seatService.cancelSeatReservation(seatId));
        assertTrue(ex.getMessage().contains("not reserved"));
    }
}