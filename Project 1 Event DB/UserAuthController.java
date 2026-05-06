package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserAuthController {

    @GetMapping("/user/login")
    public String showLoginPage() {
        return "login"; // This looks for login.html in your templates folder
    }

    @PostMapping("/user/login")
    public String handleLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              HttpSession session, 
                              Model model) {
        
        // Simple hardcoded check
        if ("user".equals(username) && "password123".equals(password)) {
            session.setAttribute("loggedInUser", username);
            return "redirect:/user"; // Redirects to the User Dashboard
        } else {
            model.addAttribute("error", "Invalid Username or Password");
            return "login";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clears the session data
        return "redirect:/user/login";
    }
}