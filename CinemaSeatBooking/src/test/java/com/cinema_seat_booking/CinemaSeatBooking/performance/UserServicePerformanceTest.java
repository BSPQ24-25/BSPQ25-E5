package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.service.UserService;
import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.*;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(JUnitPerfInterceptor.class)
public class UserServicePerformanceTest {
	@Autowired
	private UserService userService;

	@JUnitPerfTestActiveConfig
	private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
	    .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/user-service-perf-report.html"))
	    .build();

	@BeforeEach
	void setup() {
	}

	@Test
	@JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:400ms", allowedErrorPercentage = 1.0f)
	public void testRegisterUserPerformance() {
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUsername("user_" + UUID.randomUUID());
	    userDTO.setEmail(userDTO.getUsername() + "@test.com");
	    userDTO.setPassword("password");

	    User result = userService.registerUser(userDTO);
	    assertNotNull(result);
	}

	@Test
	@JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:300ms", allowedErrorPercentage = 2.0f)
	public void testGetUserByUsernamePerformance() {
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUsername("user_" + UUID.randomUUID());
	    userDTO.setEmail(userDTO.getUsername() + "@test.com");
	    userDTO.setPassword("password");

	    userService.registerUser(userDTO);
	    User result = userService.getUserByUsername(userDTO.getUsername());

	    assertNotNull(result);
	    assertEquals(userDTO.getUsername(), result.getUsername());
	}

	@Test
	@JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 0.2f)
	public void testUpdateUserProfilePerformance() {
	    String uniqueId = UUID.randomUUID().toString();
	    
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUsername("user_" + uniqueId);
	    userDTO.setEmail("user_" + uniqueId + "@test.com");
	    userDTO.setPassword("password");

	    userService.registerUser(userDTO);

	    // Update same user
	    userDTO.setEmail("updated_user_" + uniqueId + "@test.com");
	    userDTO.setPassword("newpassword");

	    User updated = userService.updateUserProfile(userDTO);

	    assertNotNull(updated);
	    assertEquals("updated_user_" + uniqueId + "@test.com", updated.getEmail());
	}

	@Test
	@JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
	@JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 0.0f)
	public void testDeleteUserPerformance() {
	    UserDTO userDTO = new UserDTO();
	    userDTO.setUsername("user_" + UUID.randomUUID());
	    userDTO.setEmail(userDTO.getUsername() + "@test.com");
	    userDTO.setPassword("password");

	    userService.registerUser(userDTO);
	    boolean deleted = userService.deleteUser(userDTO.getUsername(), userDTO.getPassword());
	    assertTrue(deleted);
	}

	@Test
	@JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 500)
	@JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:400ms", allowedErrorPercentage = 0.0f)
	public void testDeleteUserFailureCase() {
	    boolean result = userService.deleteUser("nonexistent_user", "wrongpass");
	    assertFalse(result);
	}
}
