package com.cinema_seat_booking.CinemaSeatBooking.integration;

import com.cinema_seat_booking.dto.*;
import com.cinema_seat_booking.model.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
        screeningDTO.setMovie(createdMovie); // Use the movie created in Step 3
        screeningDTO.setRoom(new RoomDTO(room)); // Use the room created in Step 2
        screeningDTO.setDate(LocalDateTime.now().plusDays(1).toString()); // Set a valid future date
        screeningDTO.setLocation("Main Theater"); // Set a valid location

        System.out.println("Sending ScreeningDTO:");
        System.out.println("  ID: " + screeningDTO.getId());
        System.out.println("  Movie: " + (screeningDTO.getMovie() != null ? screeningDTO.getMovie().getId() : "null"));
        System.out.println("  Date: " + screeningDTO.getDate());
        System.out.println("  Location: " + screeningDTO.getLocation());
        System.out.println("  Room: " + (screeningDTO.getRoom() != null ? screeningDTO.getRoom().getId() : "null"));
        HttpEntity<ScreeningDTO> screeningRequest = new HttpEntity<>(screeningDTO, headers);
        ResponseEntity<ScreeningDTO> screeningResponse = restTemplate.postForEntity(
                "/api/screenings", screeningRequest, ScreeningDTO.class);

        // if (screeningResponse.getStatusCode() != HttpStatus.CREATED) {
        //     System.out.println("Screening creation failed: " + screeningResponse.getBody());
        // }
        // assertEquals(HttpStatus.CREATED, screeningResponse.getStatusCode());
        // ScreeningDTO createdScreening = screeningResponse.getBody();
        // assertNotNull(createdScreening);
        Long screeningId = screeningDTO.getId();
        //createdScreening.getId();
        System.out.println("Created Screening ID: " + screeningId);

        // Step 5: Get available seats
        ResponseEntity<SeatDTO[]> seatResponse = restTemplate.getForEntity("/api/seats/room/" + room.getId(), SeatDTO[].class);
        assertEquals(HttpStatus.OK, seatResponse.getStatusCode());
        SeatDTO[] seats = seatResponse.getBody();
        assertNotNull(seats);
        assertTrue(seats.length > 0);
        Long seatId = seats[0].getId();
        System.out.println("seatId is : "+seatId);
        System.out.println("username"+username);
        System.out.println(screeningId+"screeningId");
        screeningId=System.currentTimeMillis();
                System.out.println(screeningId+"screeningId");


// Step 6: Create reservation
headers.setContentType(MediaType.APPLICATION_JSON);

// Build the request entity
HttpEntity<?> requestEntity = new HttpEntity<>(headers);

// Send the POST request to the ReservationController
String reservationUrl = String.format("/api/reservations?username=%s&screeningId=%d&seatId=%d", 
        username, screeningId, seatId);

ResponseEntity<ReservationDTO> reservationResponse = restTemplate.exchange(
        reservationUrl, 
        HttpMethod.GET, 
        null, 
        ReservationDTO.class
);

// Validate the response
assertEquals(HttpStatus.CREATED, reservationResponse.getStatusCode());
ReservationDTO reservation = reservationResponse.getBody();
assertNotNull(reservation, "ReservationDTO is null");
assertEquals(screeningId, reservation.getScreening().getId());
assertEquals(seatId, reservation.getSeat().getId());
Long reservationId = reservation.getReservationId();
System.out.println("Created Reservation ID: " + reservationId);

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