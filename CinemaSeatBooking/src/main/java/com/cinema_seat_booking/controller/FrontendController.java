package com.cinema_seat_booking.controller;

import com.cinema_seat_booking.model.Role;
import com.cinema_seat_booking.model.User;
import com.cinema_seat_booking.repository.MovieRepository;
import com.cinema_seat_booking.repository.UserRepository;
import com.cinema_seat_booking.service.ReservationService;
import com.cinema_seat_booking.service.RoomService;
import com.cinema_seat_booking.service.ScreeningService;
import com.cinema_seat_booking.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class FrontendController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String showLoginPage() {
        return "login"; // ← muestra login.html correctamente
    }

    private String getUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                return authority.getAuthority();
            }
        }
        return null;
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // register.html
    }
    /*
     * @GetMapping("/home")
     * public String showHomePage(HttpSession session, Model model) {
     * Object userObj = session.getAttribute("user");
     * if (userObj == null) {
     * return "redirect:/";
     * }
     * 
     * String role = getUserRole(session);
     * if ("ADMIN".equals(role)) {
     * return "redirect:/admin-dashboard";
     * }
     * 
     * model.addAttribute("movies", movieRepository.findAll());
     * return "index"; // Para CLIENT
     * }
     */
    /*
     * @PostMapping("/do-login")
     * public String fakeLoginRedirect(HttpSession session, HttpServletRequest
     * request) {
     * String username = request.getParameter("username");
     * 
     * com.cinema_seat_booking.model.User user = new
     * com.cinema_seat_booking.model.User();
     * user.setUsername(username);
     * user.setRole(com.cinema_seat_booking.model.Role.CLIENT);
     * 
     * session.setAttribute("user", user);
     * return "redirect:/home";
     * }
     */

    @PostMapping("/do-login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = userService.authenticate(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }

        session.setAttribute("user", user);

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin-dashboard";
        } else {
            return "redirect:/home";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra la sesión del usuario
        return "redirect:/"; // Redirige a la página de login o home
    }

    /*
     * @GetMapping("/home")
     * public String showHomePage(HttpSession session, Model model) {
     * // cargar películas
     * return "index"; // index.html
     * }
     */

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        model.addAttribute("movies", movieRepository.findAll()); // ← Carga las pelis
        return "index"; // index.html
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        return "admin-dashboard"; // admin-dashboard.html
    }

    @GetMapping("/myreservations")
    public String showMyReservationsPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("reservations", reservationService.getAllReservationsOfUser(user.getUsername()));
        return "myreservations"; // myreservations.html
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "about-us"; // about-us.html
    }

    @GetMapping("/404-lt")
    public String show404ltPage() {
        return "404-lt"; // about-us.html
    }

    @GetMapping("/articles")
    public String showArticlesPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            String username = auth.getName(); // nombre de usuario
        }
        return "articles";
    }

    @GetMapping("/article")
    public String showArticlePage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        return "article"; // article.html
    }

    @GetMapping("/contact")
    public String showContactPage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        return "contact-us"; // contact-us.html
    }

    @GetMapping("/rooms")
    public String showRoomsPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms"; // rooms.html
    }

    @GetMapping("/rooms/{id}")
    public String showRoomDetails(@PathVariable Long id, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("room", roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id)));
        return "room"; // room.html
    }

    @GetMapping("/screenings")
    public String showScreeningsPage(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening";
    }

    @GetMapping("/screenings/{id}")
    public String showScreeningDetails(@PathVariable Long id, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("screening", screeningService.getScreeningById(id)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + id)));
        return "reservation"; // reservation.html
    }
    // lithuanian

    @GetMapping("/register-lt")
    public String showLTRegisterPage() {
        return "register-lt"; // register.html
    }

    @GetMapping({ "/lt", "/home-lt" })
    public String showLTHomePage(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "index-lt"; // index.html
    }

    @GetMapping("/about-lt")
    public String showLTAboutPage() {
        return "about-us-lt"; // about-us.html
    }

    @GetMapping("/articles-lt")
    public String showLTArticlesPage() {
        return "articles-lt"; // articles.html
    }

    @GetMapping("/article-lt")
    public String showLTArticlePage() {
        return "article-lt"; // article.html
    }

    @GetMapping("/contact-lt")
    public String showLTContactPage() {
        return "contact-us-lt"; // contact-us.html
    }

    @GetMapping("/rooms-lt")
    public String showLTRoomsPage(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms-lt"; // rooms.html
    }

    @GetMapping("/screenings-lt")
    public String showLTScreeningsPage(Model model) {
        model.addAttribute("screenings", screeningService.getAllScreenings());
        return "screening-lt";
    }

    @GetMapping("/screenings-lt/{id}")
    public String showLTScreeningDetails(@PathVariable Long id, Model model) {
        // Obtén el screening usando el ID
        model.addAttribute("screening", screeningService.getScreeningById(id)
                .orElseThrow(() -> new IllegalArgumentException("Screening not found with ID: " + id)));
        return "reservation-lt"; // reservation.html
    }

    @PostMapping("/do-register")
    public String registerUser(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        // Verifica si ya existe el username
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists. Try another.");
            return "register"; // Vuelve al formulario con mensaje de error
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        if (email.toLowerCase().contains("admin")) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.CLIENT);
        }

        userRepository.save(user);
        session.setAttribute("user", user);

        return user.getRole() == Role.ADMIN ? "redirect:/admin-dashboard" : "redirect:/home";
    }

}
