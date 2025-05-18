package com.cinema_seat_booking.CinemaSeatBooking.integration;

import com.cinema_seat_booking.dto.*;
import com.cinema_seat_booking.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CinemaSeatIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCinemaSeatReservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 1. Register user
        String uniqueId = String.valueOf(System.nanoTime()).substring(0, 6);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("jane" + uniqueId);
        userDTO.setPassword("securePass");
        userDTO.setEmail("jane" + uniqueId + "@example.com");
        HttpEntity<UserDTO> userRequest = new HttpEntity<>(userDTO, headers);
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/api/users/register", userRequest, User.class);
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
        String username = userResponse.getBody().getUsername();

        // 2. Create movie
        Movie movie = new Movie("Test Movie " + uniqueId, 120, "Drama", "Actor1,Actor2");
        HttpEntity<Movie> movieRequest = new HttpEntity<>(movie, headers);
        ResponseEntity<Movie> movieResponse = restTemplate.postForEntity("/api/movies", movieRequest, Movie.class);
        assertEquals(HttpStatus.CREATED, movieResponse.getStatusCode());
        Long movieId = movieResponse.getBody().getId();

        // 3. Create room
        Room room = new Room();
        room.setName("Room" + uniqueId);
        HttpEntity<Room> roomRequest = new HttpEntity<>(room, headers);
        ResponseEntity<Room> roomResponse = restTemplate.postForEntity("/api/rooms", roomRequest, Room.class);
        assertEquals(HttpStatus.CREATED, roomResponse.getStatusCode());
        Long roomId = roomResponse.getBody().getId();

        // 4. Create screening
        Screening screening = new Screening();
        screening.setMovie(movieResponse.getBody());
        screening.setRoom(roomResponse.getBody());
        screening.setDate("2025-05-18T20:00:00");
        screening.setLocation("Main Theater");
        HttpEntity<Screening> screeningRequest = new HttpEntity<>(screening, headers);
        ResponseEntity<ScreeningDTO> screeningResponse = restTemplate.postForEntity("/api/screenings", screeningRequest, ScreeningDTO.class);
        assertEquals(HttpStatus.CREATED, screeningResponse.getStatusCode());
        Long screeningId = screeningResponse.getBody().getId();

        // 5. Get seats for the room
        ResponseEntity<SeatDTO[]> seatsResponse = restTemplate.getForEntity("/api/seats/room/" + roomId, SeatDTO[].class);
        assertEquals(HttpStatus.OK, seatsResponse.getStatusCode());
        SeatDTO[] seats = seatsResponse.getBody();
        assertNotNull(seats);
        assertTrue(seats.length > 0);
        Long seatId = seats[0].getId();

        // 6. Make reservation
        String reservationUrl = String.format("/api/reservations?username=%s&screeningId=%d&seatId=%d", username, screeningId, seatId);
        ResponseEntity<ReservationDTO> reservationResponse = restTemplate.postForEntity(reservationUrl, null, ReservationDTO.class);
        assertEquals(HttpStatus.CREATED, reservationResponse.getStatusCode());
        ReservationDTO reservation = reservationResponse.getBody();
        assertNotNull(reservation);
        assertEquals(username, reservation.getUser().getUsername());
        assertEquals(screeningId, reservation.getScreening().getId());
        assertEquals(seatId, reservation.getSeat().getId());
    }
}
