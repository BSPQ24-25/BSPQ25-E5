package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.ReservationService;
import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.DirtiesContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, JUnitPerfInterceptor.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReservationServicePerformanceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private SeatRepository seatRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    // Shared test data
    private User testUser;
    private Screening testScreening;
    private Room testRoom;
    private List<Seat> availableSeats = new ArrayList<>();
    private AtomicInteger seatCounter = new AtomicInteger(0);

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(
            System.getProperty("user.dir") + "/target/reports/reservation-service-perf-report.html"))
        .build();

    @BeforeAll
    @Transactional
    @Rollback(false) // We need the data for the tests, but will clean up afterward
    public void setupTestData() {
        // Create a single test user
        testUser = new User();
        testUser.setUsername("perf_test_user_" + UUID.randomUUID());
        testUser.setEmail("perf_test_" + UUID.randomUUID() + "@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        // Create movie
        Movie testMovie = new Movie();
        testMovie.setTitle("PerfTest Movie " + UUID.randomUUID());
        testMovie.setCast("Test Actor A, Test Actor B");
        testMovie.setGenre("Test");
        testMovie.setDuration(120);
        testMovie = movieRepository.save(testMovie);

        // Create room
        testRoom = new Room("PerfTest Room " + UUID.randomUUID());
        testRoom = roomRepository.save(testRoom);
        
        // Create screening
        testScreening = new Screening();
        testScreening.setMovie(testMovie);
        testScreening.setRoom(testRoom);
        testScreening.setDate("2025-05-15 18:00");
        testScreening.setLocation("Performance Test Hall");
        testScreening = screeningRepository.save(testScreening);
        
        // Create 100 test seats for this room
        for (int i = 1; i <= 100; i++) {
            Seat seat = new Seat(i, false, testRoom);
            seat = seatRepository.save(seat);
            availableSeats.add(seat);
        }
    }

    @AfterAll
    @Transactional
    @Rollback(false)
    public void cleanupTestData() {
        // Clean up all test data
        reservationRepository.deleteAll();
        screeningRepository.delete(testScreening);
        seatRepository.deleteAll(availableSeats);
        roomRepository.delete(testRoom);
        movieRepository.delete(testScreening.getMovie());
        userRepository.delete(testUser);
    }
    
    /**
     * Get an available seat for testing in a thread-safe way
     * Each test gets its own seat and returns it when done
     */
    private synchronized Seat getNextAvailableSeat() {
        int index = seatCounter.getAndIncrement() % availableSeats.size();
        return availableSeats.get(index);
    }

    @Test
    @Transactional
    @Rollback(true) // Important: rolls back all changes after test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 30, percentiles = "95:300ms", allowedErrorPercentage = 1f)
    public void testCreateReservationPerformance() {
        Seat seat = getNextAvailableSeat();
        
        Reservation result = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testScreening.getId(), result.getScreening().getId());
        assertEquals(seat.getId(), result.getSeat().getId());
        assertEquals(ReservationState.PENDING, result.getReservationState());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(true)
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:350ms", allowedErrorPercentage = 1f)
    public void testMakePaymentPerformance() {
        Seat seat = getNextAvailableSeat();
        
        // Create reservation first
        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(reservation);
        
        // Test payment processing performance
        Payment result = reservationService.makePayment(reservation.getId(), "PayPal", 30.0, "2025-05-15");
        
        assertNotNull(result);
        assertEquals("PayPal", result.getPaymentMethod());
        assertEquals(30.0, result.getAmount(), 0.001);
        assertEquals("2025-05-15", result.getPaymentDate());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(true)
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:250ms", allowedErrorPercentage = 1f)
    public void testCancelReservationPerformance() {
        Seat seat = getNextAvailableSeat();
        
        // Create a reservation first
        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(reservation);
        
        // Then test cancellation performance
        reservationService.cancelReservation(reservation.getId());
        
        // Get fresh seat to verify
        Seat freshSeat = seatRepository.findById(seat.getId()).orElse(null);
        assertNotNull(freshSeat);
        assertFalse(freshSeat.isReserved());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(true)
    @JUnitPerfTest(threads = 20, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:400ms", allowedErrorPercentage = 1f)
    public void testCreateReservationHighLoad() {
        Seat seat = getNextAvailableSeat();
        
        Reservation result = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(result);
        
        // Make some assertions to verify correctness
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testScreening.getId(), result.getScreening().getId());
        assertTrue(seat.isReserved());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(true)
    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(percentiles = "95:200ms", allowedErrorPercentage = 1f)
    public void testGetReservationByIdPerformance() {
        Seat seat = getNextAvailableSeat();
        
        // Create a reservation first
        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(reservation);
        
        // Measure getReservationById performance
        Reservation result = reservationService.getReservationById(reservation.getId());
        
        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testScreening.getId(), result.getScreening().getId());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback(true)
    @JUnitPerfTest(threads = 8, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "90:300ms,95:400ms", allowedErrorPercentage = 1f)
    public void testEndToEndReservationFlow() {
        Seat seat = getNextAvailableSeat();
        
        // 1. Create reservation
        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
        assertNotNull(reservation);
        
        // 2. Make payment
        Payment payment = reservationService.makePayment(reservation.getId(), "Credit Card", 35.0, "2025-05-15");
        assertNotNull(payment);
        
        // 3. Verify state
        Reservation updatedReservation = reservationService.getReservationById(reservation.getId());
        assertNotNull(updatedReservation.getPayment());
        
        // 4. Cancel reservation
        reservationService.cancelReservation(reservation.getId());
        
        // Force flush to accurately measure database performance
        entityManager.flush();
    }
}