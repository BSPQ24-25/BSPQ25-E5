package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.service.ScreeningService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ScreeningServicePerformanceTest {

    @Autowired
    private ScreeningService screeningService;

    // Configure HTML performance report
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/screening-perf.html"))
            .build();

    @BeforeEach
    void setup() {
        // Optional: Prepare DB data if needed
    }

    /**
     * Success test: throughput test with getAllScreenings()
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:300ms")
    public void testGetAllScreenings_Throughput() {
        assertTrue(screeningService.getAllScreenings().size() >= 0);
    }

    /**
     * Duration test: runs getScreeningsByMovie repeatedly for 8 seconds
     */
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(percentiles = "95:400ms", allowedErrorPercentage = 2.0f)
    public void testGetScreeningsByMovie_Duration() {
        assertTrue(screeningService.getScreeningsByMovie(1L).size() >= 0);
    }

    /**
     * Performance test for getScreeningById
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 25)
    public void testGetScreeningById_Performance() {
        Optional<Screening> screening = screeningService.getScreeningById(1L);
        assertTrue(screening.isPresent());
    }
}
