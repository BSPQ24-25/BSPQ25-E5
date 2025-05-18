package com.cinema_seat_booking.CinemaSeatBooking.performance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cinema_seat_booking.model.Payment;
import com.cinema_seat_booking.model.PaymentStatus;
import com.cinema_seat_booking.model.Reservation;
import com.cinema_seat_booking.model.ReservationState;
import com.cinema_seat_booking.model.Screening;
import com.cinema_seat_booking.model.Seat;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.service.PaymentService;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

@SpringBootTest(classes = com.cinema_seat_booking.controller.CinemaSeatBookingApplication.class)
@ExtendWith(JUnitPerfInterceptor.class)
public class PaymentServicePT {
	@Autowired
	private PaymentService paymentService;

	private Reservation reservation;

	@JUnitPerfTestActiveConfig
	private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
			.reportGenerator(new HtmlReportGenerator(
					System.getProperty("user.dir")
							+ "/target/site/performance-reports/payment-service-perf-report.html"))
			.build();

	@BeforeEach
	void setup() {
		User user = new User();
		user.setId(1L);
		user.setUsername("user_" + UUID.randomUUID());

		Screening screening = new Screening();
		screening.setId(1L);

		Seat seat = new Seat();
		seat.setId(1L);
		seat.setSeatNumber(1);

		reservation = new Reservation(user, screening, seat);
		reservation.setId(100L);
		reservation.setReservationState(ReservationState.PENDING);
	}

	@Test
	@JUnitPerfTest(threads = 10, durationMs = 6000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 25, percentiles = "95:300ms", allowedErrorPercentage = 1.0f)
	public void testProcessPaymentPerformance() {
		Payment result = paymentService.processPayment(reservation, "CreditCard", 50.0, "2025-05-01");
		assertNotNull(result);
		assertEquals(PaymentStatus.COMPLETED, result.getStatus());
		assertEquals(ReservationState.PAID, reservation.getReservationState());
	}

	@Test
	@JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 500)
	@JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:800ms", allowedErrorPercentage = 100.0f)
	public void testSimulatedPaymentFailurePerformance() {
		PaymentService failingPaymentService = new PaymentService() {
			public boolean simulatePaymentGateway() {
				return false;
			}
		};

		Payment failedPayment = failingPaymentService.processPayment(reservation, "Paypal", 50.0, "2025-05-01");
		assertNotNull(failedPayment);
		assertEquals(PaymentStatus.FAILED, failedPayment.getStatus());
		assertNotEquals(ReservationState.PAID, reservation.getReservationState());
	}

}
