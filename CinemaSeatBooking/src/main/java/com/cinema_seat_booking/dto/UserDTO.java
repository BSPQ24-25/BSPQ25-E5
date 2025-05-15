
package com.cinema_seat_booking.dto;

import com.cinema_seat_booking.model.User; // Correct import
import com.cinema_seat_booking.model.*;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
       @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public UserDTO() {
        // Constructor vacío requerido por Spring para deserializar JSON
    }
     public UserDTO(User user) {
    this.username = user.getUsername(); // Use the correct getter
    this.password = user.getPassword();
    this.email = user.getEmail();
}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public UserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

