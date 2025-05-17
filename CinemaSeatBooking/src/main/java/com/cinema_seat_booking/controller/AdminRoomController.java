package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.dto.ScreeningDTO;
import com.cinema_seat_booking.model.Room;
import com.cinema_seat_booking.dto.CreateRoomDTO;
import com.cinema_seat_booking.dto.RoomDTO;
import com.cinema_seat_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/rooms")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/admin-rooms";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("room", new CreateRoomDTO());
        return "admin/admin-room-form";
    }

    @PostMapping
    public String save(@ModelAttribute("room") CreateRoomDTO dto) {
        roomService.createRoomWithSeats(dto.getName(), dto.getSeatCount());
        return "redirect:/admin/rooms";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sala no encontrada: " + id));

        CreateRoomDTO form = new CreateRoomDTO();
        form.setName(room.getName());
        form.setSeatCount(room.getSeatCount());

        model.addAttribute("room", form);
        model.addAttribute("id", id);
        return "admin/admin-room-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("room") CreateRoomDTO dto) {
        roomService.updateRoom(id, dto.getName());
        return "redirect:/admin/rooms";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }
}
