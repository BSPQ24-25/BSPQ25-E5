
package com.cinema_seat_booking.dto;

public class UserDTO {
    private String username;
    private String password;
    private String email;
    
    public UserDTO() {
        // Constructor vacío requerido por Spring para deserializar JSON
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
}

