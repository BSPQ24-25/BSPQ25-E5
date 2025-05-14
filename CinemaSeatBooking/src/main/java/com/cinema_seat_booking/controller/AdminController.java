package com.cinema_seat_booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminOnly() {
        return null;
    }
}
