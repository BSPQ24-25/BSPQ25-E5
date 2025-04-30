package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.*;
import com.cinema_seat_booking.service.ReservationService;
import com.github.noconnor.junitperf.*;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
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

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(
            System.getProperty("user.dir") + "/target/reports/reservation-service-perf-report.html"))
        .build();

    private User generateUniqueUser() {
        User user = new User();
        String unique = UUID.randomUUID().toString();
        user.setUsername("user_" + unique);
        user.setEmail("user_" + unique + "@example.com");
        user.setPassword("password");
        return userRepository.save(user);
    }

    private Screening generateScreening() {
        Movie movie = new Movie();
        movie.setTitle("PerfTest Movie " + UUID.randomUUID());
        movie.setCast("Actor A, Actor B");
        movie.setGenre("Action");
        movie.setDuration(1);
        movie = movieRepository.save(movie);

        Room room = new Room("PerfTest Room " + UUID.randomUUID());
        room = roomRepository.save(room);

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setRoom(room);
        screening.setDate("2025-04-30 18:00");
        screening.setLocation("Main Hall");
        return screeningRepository.save(screening);
    }

    private Seat generateSeat(Room room) {
        Seat seat = new Seat((int) (Math.random() * 1000), room);
        seat.setReserved(false);
        return seatRepository.save(seat);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:500ms", allowedErrorPercentage = 0.2f)
    public void testCreateReservationPerformance() {
        User user = generateUniqueUser();
        Screening screening = generateScreening();
        Seat seat = generateSeat(screening.getRoom());

        Reservation result = reservationService.createReservation(user, screening, seat, "Credit");
        assertNotNull(result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 15, percentiles = "95:400ms", allowedErrorPercentage = 0.2f)
    public void testMakePaymentThroughput() {
        User user = generateUniqueUser();
        Screening screening = generateScreening();
        Seat seat = generateSeat(screening.getRoom());

        Reservation reservation = reservationService.createReservation(user, screening, seat, "Credit");
        Payment result = reservationService.makePayment(reservation.getId(), "PayPal", 30.0, "2025-04-30");
        assertNotNull(result);
    }

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 15, percentiles = "95:300ms", allowedErrorPercentage = 0.1f)
    public void testCancelReservationPerformance() {
        User user = generateUniqueUser();
        Screening screening = generateScreening();
        Seat seat = generateSeat(screening.getRoom());

        Reservation reservation = reservationService.createReservation(user, screening, seat, "Credit");
        reservationService.cancelReservation(reservation.getId());

        assertFalse(seatRepository.findById(seat.getId()).get().isReserved());
    }

    @Test
    @JUnitPerfTest(threads = 20, durationMs = 10000, warmUpMs = 2000)
    @JUnitPerfTestRequirement(executionsPerSec = 30, percentiles = "95:600ms", allowedErrorPercentage = 0.2f)
    public void testCreateReservationHighLoad() {
        User user = generateUniqueUser();
        Screening screening = generateScreening();
        Seat seat = generateSeat(screening.getRoom());

        Reservation result = reservationService.createReservation(user, screening, seat, "Credit");
        assertNotNull(result);
    }

    @Test
    @JUnitPerfTest(threads = 5, durationMs = 10000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(percentiles = "95:500ms", allowedErrorPercentage = 0.1f)
    public void testReservationDurationPerformance() {
        User user = generateUniqueUser();
        Screening screening = generateScreening();
        Seat seat = generateSeat(screening.getRoom());

        Reservation result = reservationService.createReservation(user, screening, seat, "Credit");
        assertNotNull(result);
    }
}


