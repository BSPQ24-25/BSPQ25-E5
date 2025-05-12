package com.cinema_seat_booking.CinemaSeatBooking.integration;

import com.cinema_seat_booking.dto.CreateRoomDTO;
import com.cinema_seat_booking.dto.ReservationDTO;
import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.dto.UserDTO;
import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateAndCancelReservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Step 1: Register user
        String uniqueId = String.valueOf(System.nanoTime()).substring(0, 6);
        UserDTO userDTO = new UserDTO(
            "jane" + uniqueId,
            "securePass",
            "jane" + uniqueId + "@example.com"
        );
        HttpEntity<UserDTO> userRequest = new HttpEntity<>(userDTO, headers);
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/api/users/register", userRequest, User.class);

        if (userResponse.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("User registration failed: " + userResponse.getBody());
        }
        assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
        User user = userResponse.getBody();
        assertNotNull(user);
        String username = user.getUsername();

        // Step 2: Create room
        CreateRoomDTO createRoomDTO = new CreateRoomDTO("Room" + System.currentTimeMillis());
        System.out.println("Sending CreateRoomDTO: " + createRoomDTO);

        //HttpEntity<CreateRoomDTO> roomRequest = new HttpEntity<>(createRoomDTO, headers);
        ResponseEntity<Room> roomResponse = restTemplate.postForEntity("/api/rooms", createRoomDTO, Room.class);

        if (roomResponse.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Room creation failed: " + roomResponse.getBody());
        }
        assertEquals(HttpStatus.CREATED, roomResponse.getStatusCode());
        Room room = roomResponse.getBody();
        assertNotNull(room);

        // Step 3: Create movie
        Movie movie = new Movie("Movie" + uniqueId, 120, "Drama", "Cast1");
        HttpEntity<Movie> movieRequest = new HttpEntity<>(movie, headers);
        ResponseEntity<Movie> movieResponse = restTemplate.postForEntity("/api/movies", movieRequest, Movie.class);

        if (movieResponse.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Movie creation failed: " + movieResponse.getBody());
        }
        assertEquals(HttpStatus.CREATED, movieResponse.getStatusCode());
        Movie createdMovie = movieResponse.getBody();
        assertNotNull(createdMovie);
        Long movieId = createdMovie.getId();

        // Step 4: Create screening
        ScreeningDTO screeningDTO = new ScreeningDTO();
        screeningDTO.setMovie(createdMovie);
        screeningDTO.setRoom(new RoomDTO(room)); // Convert Room to RoomDTO

        screeningDTO.setDate(LocalDateTime.now().plusDays(1).toString());
        screeningDTO.setLocation("Main Theater");

        HttpEntity<ScreeningDTO> screeningRequest = new HttpEntity<>(screeningDTO, headers);
        ResponseEntity<ScreeningDTO> screeningResponse = restTemplate.postForEntity(
                "/api/screenings", screeningRequest, ScreeningDTO.class);

        if (screeningResponse.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Screening creation failed: " + screeningResponse.getBody());
        }
        assertEquals(HttpStatus.CREATED, screeningResponse.getStatusCode());
        ScreeningDTO createdScreening = screeningResponse.getBody();
        assertNotNull(createdScreening);
        Long screeningId = createdScreening.getId();

        // Step 5: Get available seats
        ResponseEntity<Seat[]> seatResponse = restTemplate.getForEntity("/api/seats/room/" + room.getId(), Seat[].class);
        assertEquals(HttpStatus.OK, seatResponse.getStatusCode());
        Seat[] seats = seatResponse.getBody();
        assertNotNull(seats);
        assertTrue(seats.length > 0);
        Long seatId = seats[0].getId();

        // Step 6: Create reservation
        String reservationUrl = String.format("/api/reservations?username=%s&screeningId=%d&seatId=%d",
                username, screeningId, seatId);

        ResponseEntity<ReservationDTO> reservationResponse =
                restTemplate.postForEntity(reservationUrl, null, ReservationDTO.class);

        if (reservationResponse.getStatusCode() != HttpStatus.OK) {
            System.out.println("Reservation creation failed: " + reservationResponse.getBody());
        }
        assertEquals(HttpStatus.OK, reservationResponse.getStatusCode());
        ReservationDTO reservation = reservationResponse.getBody();
        assertNotNull(reservation);
        Long reservationId = reservation.getReservationId();

        // Step 7: Fetch reservation
        ResponseEntity<ReservationDTO> fetchedResponse =
                restTemplate.getForEntity("/api/reservations/" + reservationId, ReservationDTO.class);

        if (fetchedResponse.getStatusCode() != HttpStatus.OK) {
            System.out.println("Fetching reservation failed: " + fetchedResponse.getBody());
        }
        assertEquals(HttpStatus.OK, fetchedResponse.getStatusCode());
        ReservationDTO fetched = fetchedResponse.getBody();
        assertNotNull(fetched);
        assertEquals(screeningId, fetched.getScreening().getId());
        assertEquals(seatId, fetched.getSeat().getId());

        // Step 8: Cancel reservation
        restTemplate.delete("/api/reservations/" + reservationId);

        // Step 9: Try fetching again
        ResponseEntity<ReservationDTO> afterDeleteResponse =
                restTemplate.getForEntity("/api/reservations/" + reservationId, ReservationDTO.class);

        System.out.println("Post-delete fetch status: " + afterDeleteResponse.getStatusCode());
        assertTrue(afterDeleteResponse.getStatusCode() == HttpStatus.NOT_FOUND ||
                   afterDeleteResponse.getBody() == null);
    }
    @AfterEach
        void cleanup() {
        restTemplate.delete("/api/rooms/deleteAll");
}
}