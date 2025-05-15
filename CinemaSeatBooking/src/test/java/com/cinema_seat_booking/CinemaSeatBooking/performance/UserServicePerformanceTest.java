package com.cinema_seat_booking.CinemaSeatBooking.performance;

import com.cinema_seat_booking.service.UserService;
import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.*;
import com.cinema_seat_booking.repository.UserRepository;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith({JUnitPerfInterceptor.class, SpringExtension.class})
public class UserServicePerformanceTest {
    
    // Thread-safe counters for generating unique usernames within tests
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    // For non-transaction tests, we'll use this mock-based setup
    @Mock
    private UserRepository mockUserRepository;
    
    @InjectMocks
    private UserService mockUserService;
    
    private AutoCloseable closeable;

    @JUnitPerfTestActiveConfig
    private static final JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
        .reportGenerator(new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/user-service-perf-report.html"))
        .build();

    @BeforeEach
    void setup() {
        // Initialize mocks
        closeable = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    void cleanup() throws Exception {
        // Release mocks
        if (closeable != null) {
            closeable.close();
        }
    }
    
    /**
     * Creates a truly unique username for each thread and invocation
     */
    private String generateUniqueUsername(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString() + "_" + 
               System.currentTimeMillis() + "_" + 
               COUNTER.incrementAndGet();
    }

    /**
     * This test uses mocks to avoid database operations
     */
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 8000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 20, percentiles = "95:400ms", allowedErrorPercentage = 1f)
    public void testRegisterUserPerformance() {
        // Generate unique username for this test invocation
        String uniqueUsername = generateUniqueUsername("reg_user");
        
        // Setup mock behavior - for each call, return a user with the unique username
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Simulate database assigning ID
            return savedUser;
        });
        
        // Test with mock
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(uniqueUsername);
        userDTO.setEmail(uniqueUsername + "@test.com");
        userDTO.setPassword("password");

        User result = mockUserService.registerUser(userDTO);
        
        assertNotNull(result);
        assertEquals(uniqueUsername, result.getUsername());
        
        // Verify mock was called
        verify(mockUserRepository).save(any(User.class));
    }

    /**
     * This test uses mocks to avoid database operations
     */
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:300ms", allowedErrorPercentage = 1f)
    public void testGetUserByUsernamePerformance() {
        // Generate unique username for this test invocation
        String uniqueUsername = generateUniqueUsername("get_user");
        
        // Create a mock user with this username
        User mockUser = new User(uniqueUsername, "password", uniqueUsername + "@test.com");
        mockUser.setId(1L);
        
        // Setup mock repository to return our mock user
        when(mockUserRepository.findByUsername(uniqueUsername)).thenReturn(mockUser);
        
        // Test performance of getting the user using mock
        User result = mockUserService.getUserByUsername(uniqueUsername);

        assertNotNull(result);
        assertEquals(uniqueUsername, result.getUsername());
        
        // Verify mock was called
        verify(mockUserRepository).findByUsername(uniqueUsername);
    }

    /**
     * This test uses mocks to avoid database operations
     */
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 6000, warmUpMs = 1000)
    @JUnitPerfTestRequirement(executionsPerSec = 10, percentiles = "95:400ms", allowedErrorPercentage = 1f)
    public void testUpdateUserProfilePerformance() {
        // Generate unique username for this test invocation
        String uniqueUsername = generateUniqueUsername("update_user");
        
        // Setup mock user
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername(uniqueUsername);
        existingUser.setEmail(uniqueUsername + "@test.com");
        existingUser.setPassword("password");
        existingUser.setRole(Role.CLIENT);
        
        // Setup mock behavior
        when(mockUserRepository.findByUsername(uniqueUsername)).thenReturn(existingUser);
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Prepare update DTO
        UserDTO updateDTO = new UserDTO();
        updateDTO.setUsername(uniqueUsername);
        updateDTO.setEmail("updated_" + uniqueUsername + "@test.com");
        updateDTO.setPassword("newpassword");

        // Perform update using mocks
        User updated = mockUserService.updateUserProfile(updateDTO);

        assertNotNull(updated);
        assertEquals("updated_" + uniqueUsername + "@test.com", updated.getEmail());
        
        // Verify mocks were called
        verify(mockUserRepository).findByUsername(uniqueUsername);
        verify(mockUserRepository).save(any(User.class));
    }

    /**
     * This test uses mocks for the negative case
     */
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 4000, warmUpMs = 500)
    @JUnitPerfTestRequirement(executionsPerSec = 5, percentiles = "95:400ms", allowedErrorPercentage = 1f)
    public void testDeleteUserFailureCase() {
        // Generate unique non-existent username
        String nonExistentUser = generateUniqueUsername("nonexistent");
        
        // Setup mock behavior for non-existent user
        when(mockUserRepository.findByUsername(nonExistentUser)).thenReturn(null);
        
        // Test the failure case
        boolean result = mockUserService.deleteUser(nonExistentUser, "wrongpass");
        assertFalse(result);
        
        // Verify mock was called
        verify(mockUserRepository).findByUsername(nonExistentUser);
    }
}