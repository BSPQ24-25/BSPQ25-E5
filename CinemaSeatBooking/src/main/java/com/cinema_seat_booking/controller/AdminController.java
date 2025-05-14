package com.cinema_seat_booking.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String adminOnly() {
        return "admin-dashboard"; // busca admin-dashboard.html en /templates
    }
}
