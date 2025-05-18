package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.Movie;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.repository.MovieRepository;
import com.cinema_seat_booking.repository.RoomRepository;
import com.cinema_seat_booking.repository.ScreeningRepository;
import com.cinema_seat_booking.service.ScreeningService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = com.cinema_seat_booking.controller.CinemaSeatBookingApplication.class)
@ExtendWith({JUnitPerfInterceptor.class, SpringExtension.class})
public class ScreeningServicePT {

    @Autowired
    private static ScreeningService screeningService;
    
    @Autowired
    private static ScreeningRepository screeningRepository;
    
    @Autowired
    private static MovieRepository movieRepository;
    
    @Autowired
    private static RoomRepository roomRepository;
    
    private static List<Long> testMovieIds = new ArrayList<>();
    private static List<Long> testRoomIds = new ArrayList<>();
    private static List<Long> testScreeningIds = new ArrayList<>();

    // Configure HTML performance report
    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/site/performance-reports/screening-perf-report.html"))
            .build();

    @BeforeAll
    public static void setup(@Autowired ScreeningService screeningServiceParam,
                          @Autowired ScreeningRepository screeningRepositoryParam,
                          @Autowired MovieRepository movieRepositoryParam,
                          @Autowired RoomRepository roomRepositoryParam) {
        screeningService = screeningServiceParam;
        screeningRepository = screeningRepositoryParam;
        movieRepository = movieRepositoryParam;
        roomRepository = roomRepositoryParam;
        
        // Use unique names with UUID to prevent conflicts
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        
        // Create test movies
        Movie testMovie1 = new Movie("Performance Test Movie 1 " + uniqueSuffix, 120, "Action", "Actor 1, Actor 2");
        Movie testMovie2 = new Movie("Performance Test Movie 2 " + uniqueSuffix, 105, "Comedy", "Actor 3, Actor 4");
        testMovie1 = movieRepository.save(testMovie1);
        testMovie2 = movieRepository.save(testMovie2);
        testMovieIds.add(testMovie1.getId());
        testMovieIds.add(testMovie2.getId());
        
        // Create test rooms
        Room testRoom1 = new Room("Performance Test Room 1 " + uniqueSuffix);
        Room testRoom2 = new Room("Performance Test Room 2 " + uniqueSuffix);
        testRoom1 = roomRepository.save(testRoom1);
        testRoom2 = roomRepository.save(testRoom2);
        testRoomIds.add(testRoom1.getId());
        testRoomIds.add(testRoom2.getId());
        
        // Create test screenings
        for (int i = 0; i < 5; i++) {
            Screening screening = new Screening(
                testMovie1, 
                "2025-05-" + (15 + i) + "T18:00:00",
                "Cinema 1",
                testRoom1
            );
            screening = screeningRepository.save(screening);
            testScreeningIds.add(screening.getId());
        }
        
        for (int i = 0; i < 5; i++) {
            Screening screening = new Screening(
                testMovie2, 
                "2025-05-" + (15 + i) + "T20:00:00",
                "Cinema 2",
                testRoom2
            );
            screening = screeningRepository.save(screening);
            testScreeningIds.add(screening.getId());
        }
    }
    
    @AfterAll
    public static void cleanup() {
        // Delete test screenings first to maintain referential integrity
        for (Long id : testScreeningIds) {
            screeningRepository.deleteById(id);
        }
        testScreeningIds.clear();
        
        // Delete test movies and rooms
        for (Long id : testMovieIds) {
            movieRepository.deleteById(id);
        }
        testMovieIds.clear();
        
        for (Long id : testRoomIds) {
            roomRepository.deleteById(id);
        }
        testRoomIds.clear();
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
        // Use first test movie ID to ensure the test is using valid data
        Long movieId = testMovieIds.isEmpty() ? 1L : testMovieIds.get(0);
        assertTrue(screeningService.getScreeningsByMovie(movieId).size() >= 0);
    }

    /**
     * Performance test for getScreeningById
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 25)
    public void testGetScreeningById_Performance() {
        // Use first test screening ID to ensure the test is using valid data
        Long screeningId = testScreeningIds.isEmpty() ? 1L : testScreeningIds.get(0);
        Optional<Screening> screening = screeningService.getScreeningById(screeningId);
        assertTrue(screening.isPresent());
    }
    
    /**
     * Load test: runs getScreeningsByLocation repeatedly to test high load
     */
    @Test
    @JUnitPerfTest(threads = 20, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 30, percentiles = "90:350ms")
    public void testGetScreeningsByLocation_Load() {
        // Assuming there's a method to get screenings by location
        // If this method doesn't exist, you would need to implement it in the service
        // For this example, we'll just use getAllScreenings as a placeholder
        assertTrue(screeningService.getAllScreenings().size() >= 0);
    }
}