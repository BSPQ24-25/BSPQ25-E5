package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.service.ReservationService;
import com.cinema_seat_booking.model.*;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class ReservationServicePerformanceTest {

	@Autowired
	private ReservationService reservationService;

	private User user;
	private Screening screening;

	@JUnitPerfTestActiveConfig
	private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
			.reportGenerator(new HtmlReportGenerator(
					System.getProperty("user.dir") + "/target/reports/reservation-service-perf-report.html"))
			.build();

	@BeforeEach
	void setup() {
		user = new User();
		user.setId(1L);

		screening = new Screening();
		screening.setId(2L);
	}

	// SUCCESSFUL: reservation creation with moderate expectations
	@Test
	@JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:500ms", allowedErrorPercentage = 1.0f)
	public void testCreateReservationPerformance() {
		Seat testSeat = new Seat();
		testSeat.setId(System.nanoTime()); // dynamic ID per thread
		testSeat.setSeatNumber((int) (Math.random() * 1000));
		testSeat.setReserved(false);

		Reservation result = reservationService.createReservation(user, screening, testSeat, "Credit");
		assertNotNull(result);
	}

	// SUCCESSFUL: makePayment using a dynamic reservation created per thread
	@Test
	@JUnitPerfTest(threads = 10, durationMs = 5000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 15, percentiles = "95:400ms", allowedErrorPercentage = 2.0f)
	public void testMakePaymentThroughput() {
		Seat seat = new Seat();
		seat.setId(System.nanoTime());
		seat.setSeatNumber((int) (Math.random() * 1000));
		seat.setReserved(false);

		Reservation res = reservationService.createReservation(user, screening, seat, "Credit");

		Payment result = reservationService.makePayment(res.getId(), "PayPal", 30.0, "2025-04-30");
		assertNotNull(result);
	}

	// SUCCESSFUL: cancellation of dynamically created reservation
	@Test
	@JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 15, percentiles = "95:300ms", allowedErrorPercentage = 2.0f)
	public void testCancelReservationPerformance() {
		Seat seat = new Seat();
		seat.setId(System.nanoTime());
		seat.setSeatNumber((int) (Math.random() * 1000));
		seat.setReserved(false);

		Reservation res = reservationService.createReservation(user, screening, seat, "Credit");
		reservationService.cancelReservation(res.getId());
	}

	// SUCCESSFUL: createReservation under heavier load but realistic expectations
	@Test
	@JUnitPerfTest(threads = 20, durationMs = 10000, warmUpMs = 2000)
	@JUnitPerfTestRequirement(executionsPerSec = 30, percentiles = "95:600ms", allowedErrorPercentage = 3.0f)
	public void testCreateReservationHighLoad() {
		Seat testSeat = new Seat();
		testSeat.setId(System.nanoTime());
		testSeat.setSeatNumber((int) (Math.random() * 1000));
		testSeat.setReserved(false);

		Reservation result = reservationService.createReservation(user, screening, testSeat, "Credit");
		assertNotNull(result);
	}

	// SUCCESSFUL: Duration-based load simulation
	@Test
	@JUnitPerfTest(threads = 5, durationMs = 10000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(percentiles = "95:500ms", allowedErrorPercentage = 2.0f)
	public void testReservationDurationPerformance() {
		Seat testSeat = new Seat();
		testSeat.setId(System.nanoTime());
		testSeat.setSeatNumber((int) (Math.random() * 1000));
		testSeat.setReserved(false);

		Reservation result = reservationService.createReservation(user, screening, testSeat, "Credit");
		assertNotNull(result);
	}
}