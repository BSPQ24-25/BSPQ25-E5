package com.cinema_seat_booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cinema_seat_booking.dto.CreateRoomDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.service.RoomService;
import org.springframework.ui.Model;
@Controller
@RequestMapping("/admin") 
public class AdminController {

    
    @GetMapping("/dashboard") 
    public String dashboard(Model model) {
        return "admin-dashboard";
    }
}
