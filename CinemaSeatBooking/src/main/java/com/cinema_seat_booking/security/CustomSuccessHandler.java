package com.cinema_seat_booking.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirectURL = "/home.html"; // valor por defecto

        for (GrantedAuthority authority : authentication.getAuthorities()) {
        	if (authority.getAuthority().equals("ADMIN")) {
        	    redirectURL = "/admin/dashboard";
        	} else if (authority.getAuthority().equals("CLIENT")) {
        	    redirectURL = "/user/home";
        	}

        }

        response.sendRedirect(redirectURL);
    }
}
