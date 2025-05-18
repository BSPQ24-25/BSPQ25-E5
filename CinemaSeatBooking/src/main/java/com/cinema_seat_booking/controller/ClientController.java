package com.cinema_seat_booking.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ClientController {
    @GetMapping("/user/home")
    public String userHome() {
        return "home"; // Spring buscará templates/home.html
    }
}