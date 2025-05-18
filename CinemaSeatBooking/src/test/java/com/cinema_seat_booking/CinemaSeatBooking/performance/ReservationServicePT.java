//
//package com.cinema_seat_booking.CinemaSeatBooking.performance;
//
//import com.cinema_seat_booking.model.*;
//import com.cinema_seat_booking.repository.*;
//import com.cinema_seat_booking.service.ReservationService;
//import com.github.noconnor.junitperf.*;
//import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.test.annotation.DirtiesContext;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = com.cinema_seat_booking.controller.CinemaSeatBookingApplication.class)
//@ExtendWith({SpringExtension.class, JUnitPerfInterceptor.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//public class ReservationServicePT {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private MovieRepository movieRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private ScreeningRepository screeningRepository;
//
//    @Autowired
//    private SeatRepository seatRepository;
//    
//    @Autowired
//    private ReservationRepository reservationRepository;
//    
//    @Autowired
//    private PaymentRepository paymentRepository;
//    
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    // Shared test data
//    private User testUser;
//    private Screening testScreening;
//    private Room testRoom;
//    private Movie testMovie;
//    private List<Seat> availableSeats = new ArrayList<>();
//    private AtomicInteger seatCounter = new AtomicInteger(0);
//    
//    // Track test resources for cleanup
//    private ConcurrentHashMap<Long, Reservation> createdReservations = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Long, Payment> createdPayments = new ConcurrentHashMap<>();
//
//    @JUnitPerfTestActiveConfig
//    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
//        .reportGenerator(new HtmlReportGenerator(
//            System.getProperty("user.dir") + "/target/reports/reservation-service-perf-report.html"))
//        .build();
//
//    @BeforeAll
//    public void setupTestData() {
//        createTestData();
//    }
//    
//    @Transactional
//    @Rollback(false)
//    public void createTestData() {
//        // Create a single test user
//        testUser = new User();
//        testUser.setUsername("perf_test_user_" + UUID.randomUUID());
//        testUser.setEmail("perf_test_" + UUID.randomUUID() + "@example.com");
//        testUser.setPassword("password");
//        testUser.setRole(Role.CLIENT);
//        testUser = userRepository.save(testUser);
//
//        // Create movie
//        testMovie = new Movie();
//        testMovie.setTitle("PerfTest Movie " + UUID.randomUUID());
//        testMovie.setCast("Test Actor A, Test Actor B");
//        testMovie.setGenre("Test");
//        testMovie.setDuration(120);
//        testMovie = movieRepository.save(testMovie);
//
//        // Create room
//        testRoom = new Room("PerfTest Room " + UUID.randomUUID());
//        testRoom = roomRepository.save(testRoom);
//        
//        // Create screening
//        testScreening = new Screening();
//        testScreening.setMovie(testMovie);
//        testScreening.setRoom(testRoom);
//        testScreening.setDate("2025-05-15 18:00");
//        testScreening.setLocation("Performance Test Hall");
//        testScreening = screeningRepository.save(testScreening);
//        
//        // Create 100 test seats for this room
//        for (int i = 1; i <= 100; i++) {
//            Seat seat = new Seat(i, false, testRoom);
//            seat = seatRepository.save(seat);
//            availableSeats.add(seat);
//        }
//        
//        // Flush and clear to ensure all entities are persisted
//        entityManager.flush();
//        entityManager.clear();
//    }
//
//    @AfterAll
//    public void cleanupTestData() {
//        cleanupAllData();
//    }
//    
//    @Transactional
//    @Rollback(false)
//    public void cleanupAllData() {
//        // Clean up all reservations created during tests
//        for (Reservation reservation : createdReservations.values()) {
//            try {
//                // Try to cancel through service first (handles relationships cleanly)
//                reservationService.cancelReservation(reservation.getId());
//            } catch (Exception e) {
//                // Fallback: Manual cleanup
//                try {
//                    Reservation managedReservation = reservationRepository.findById(reservation.getId()).orElse(null);
//                    if (managedReservation != null) {
//                        Payment payment = managedReservation.getPayment();
//                        if (payment != null) {
//                            payment.setReservation(null);
//                            paymentRepository.save(payment);
//                        }
//                        
//                        Seat seat = managedReservation.getSeat();
//                        if (seat != null) {
//                            seat.setReserved(false);
//                            seat.setReservation(null);
//                            seatRepository.save(seat);
//                        }
//                        
//                        managedReservation.setPayment(null);
//                        managedReservation.setSeat(null);
//                        managedReservation.setUser(null);
//                        managedReservation.setScreening(null);
//                        reservationRepository.save(managedReservation);
//                        reservationRepository.delete(managedReservation);
//                    }
//                } catch (Exception ex) {
//                    System.err.println("Error cleaning up reservation: " + ex.getMessage());
//                }
//            }
//        }
//        
//        // Reset all seats
//        for (Seat seat : availableSeats) {
//            try {
//                Seat managedSeat = seatRepository.findById(seat.getId()).orElse(null);
//                if (managedSeat != null) {
//                    managedSeat.setReserved(false);
//                    managedSeat.setReservation(null);
//                    seatRepository.save(managedSeat);
//                }
//            } catch (Exception e) {
//                System.err.println("Error resetting seat: " + e.getMessage());
//            }
//        }
//        
//        // Clear tracking collections
//        createdReservations.clear();
//        createdPayments.clear();
//        
//        // Flush changes
//        entityManager.flush();
//        
//        // Delete all payments that might be orphaned
//        paymentRepository.deleteAll();
//        
//        // Delete all test seats
//        seatRepository.deleteAll(availableSeats);
//        availableSeats.clear();
//        
//        // Delete screening, movie, room, and user
//        screeningRepository.delete(testScreening);
//        movieRepository.delete(testMovie);
//        roomRepository.delete(testRoom);
//        userRepository.delete(testUser);
//        
//        // Final flush
//        entityManager.flush();
//    }
//    
//    /**
//     * Get an available seat for testing in a thread-safe way
//     */
//    private synchronized Seat getNextAvailableSeat() {
//        int index = seatCounter.getAndIncrement() % availableSeats.size();
//        Seat seat = availableSeats.get(index);
//        
//        // Refresh the seat to ensure we have current state
//        seat = seatRepository.findById(seat.getId()).orElse(seat);
//        
//        // If this seat is already reserved, find another one
//        if (seat.isReserved()) {
//            for (Seat alternateSeat : availableSeats) {
//                Seat fresh = seatRepository.findById(alternateSeat.getId()).orElse(alternateSeat);
//                if (!fresh.isReserved()) {
//                    return fresh;
//                }
//            }
//        }
//        
//        return seat;
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
//    @JUnitPerfTestRequirement(executionsPerSec = 30, percentiles = "95:300ms", allowedErrorPercentage = 1f)
//    public void testCreateReservationPerformance() {
//        // Each test iteration runs in its own transaction
//        createAndTrackReservation();
//    }
//    
//    @Transactional
//    public void createAndTrackReservation() {
//        Seat seat = getNextAvailableSeat();
//        
//        Reservation result = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(result);
//        assertNotNull(result.getId());
//        assertEquals(testUser.getId(), result.getUser().getId());
//        assertEquals(testScreening.getId(), result.getScreening().getId());
//        assertEquals(seat.getId(), result.getSeat().getId());
//        assertEquals(ReservationState.PENDING, result.getReservationState());
//        
//        // Track for cleanup
//        createdReservations.put(result.getId(), result);
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
//    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:350ms", allowedErrorPercentage = 1f)
//    public void testMakePaymentPerformance() {
//        makePaymentAndTrack();
//    }
//    
//    @Transactional
//    public void makePaymentAndTrack() {
//        Seat seat = getNextAvailableSeat();
//        
//        // Create reservation first
//        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(reservation);
//        createdReservations.put(reservation.getId(), reservation);
//        
//        // Test payment processing performance
//        Payment result = reservationService.makePayment(reservation.getId(), "PayPal", 30.0, "2025-05-15");
//        
//        assertNotNull(result);
//        assertEquals("PayPal", result.getPaymentMethod());
//        assertEquals(30.0, result.getAmount(), 0.001);
//        assertEquals("2025-05-15", result.getPaymentDate());
//        
//        // Track payment for cleanup
//        createdPayments.put(result.getId(), result);
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
//    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:250ms", allowedErrorPercentage = 1f)
//    public void testCancelReservationPerformance() {
//        createAndCancelReservation();
//    }
//    
//    @Transactional
//    public void createAndCancelReservation() {
//        Seat seat = getNextAvailableSeat();
//        
//        // Create a reservation first
//        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(reservation);
//        
//        // Then test cancellation performance
//        reservationService.cancelReservation(reservation.getId());
//        
//        // Get fresh seat to verify
//        Seat freshSeat = seatRepository.findById(seat.getId()).orElse(null);
//        assertNotNull(freshSeat);
//        assertFalse(freshSeat.isReserved());
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 20, durationMs = 10000, warmUpMs = 2000)
//    @JUnitPerfTestRequirement(executionsPerSec = 40, percentiles = "95:400ms", allowedErrorPercentage = 1f)
//    public void testCreateReservationHighLoad() {
//        highLoadReservationTest();
//    }
//    
//    @Transactional
//    public void highLoadReservationTest() {
//        Seat seat = getNextAvailableSeat();
//        
//        Reservation result = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(result);
//        
//        // Track for cleanup
//        createdReservations.put(result.getId(), result);
//        
//        // Make some assertions to verify correctness
//        assertEquals(testUser.getId(), result.getUser().getId());
//        assertEquals(testScreening.getId(), result.getScreening().getId());
//        assertTrue(seat.isReserved());
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 5, durationMs = 5000, warmUpMs = 1000)
//    @JUnitPerfTestRequirement(percentiles = "95:200ms", allowedErrorPercentage = 1f)
//    public void testGetReservationByIdPerformance() {
//        getReservationByIdTest();
//    }
//    
//    @Transactional
//    public void getReservationByIdTest() {
//        Seat seat = getNextAvailableSeat();
//        
//        // Create a reservation first
//        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(reservation);
//        createdReservations.put(reservation.getId(), reservation);
//        
//        // Measure getReservationById performance
//        Reservation result = reservationService.getReservationById(reservation.getId());
//        
//        assertNotNull(result);
//        assertEquals(reservation.getId(), result.getId());
//        assertEquals(testUser.getId(), result.getUser().getId());
//        assertEquals(testScreening.getId(), result.getScreening().getId());
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 8, durationMs = 5000, warmUpMs = 1000)
//    @JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "90:300ms,95:400ms", allowedErrorPercentage = 1f)
//    public void testEndToEndReservationFlow() {
//        endToEndReservationFlowTest();
//    }
//    
//    @Transactional
//    public void endToEndReservationFlowTest() {
//        Seat seat = getNextAvailableSeat();
//        
//        // 1. Create reservation
//        Reservation reservation = reservationService.createReservation(testUser, testScreening, seat, "Credit");
//        assertNotNull(reservation);
//        
//        // 2. Make payment
//        Payment payment = reservationService.makePayment(reservation.getId(), "Credit Card", 35.0, "2025-05-15");
//        assertNotNull(payment);
//        
//        // 3. Verify state
//        Reservation updatedReservation = reservationService.getReservationById(reservation.getId());
//        assertNotNull(updatedReservation.getPayment());
//        
//        // 4. Cancel reservation
//        reservationService.cancelReservation(reservation.getId());
//        
//        // Force flush to accurately measure database performance
//        entityManager.flush();
//    }
//}