package com.cinema_seat_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {

    @GetMapping("/client/home")
    public String clientHome() {
        return "client-home"; // Apunta a src/main/resources/templates/client-home.html
    }
}
