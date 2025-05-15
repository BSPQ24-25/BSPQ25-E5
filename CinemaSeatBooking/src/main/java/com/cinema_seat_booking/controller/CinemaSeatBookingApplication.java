package com.cinema_seat_booking.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.cinema_seat_booking")
@EnableJpaRepositories(basePackages = "com.cinema_seat_booking.repository")
@EntityScan(basePackages = "com.cinema_seat_booking.model")
public class CinemaSeatBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaSeatBookingApplication.class, args);
	}

}
