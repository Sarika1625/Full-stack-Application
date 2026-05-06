package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
@Controller
public class AdminAuthController {
	@GetMapping("/admin/login")
    public String showAdminLoginPage() {
        return "admin-login"; 
    }

    @PostMapping("/admin/login")
    public String handleAdminLogin(@RequestParam String username, 
                                   @RequestParam String password, 
                                   HttpSession session, 
                                   Model model) {
        
        // Use separate credentials for admin security
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin"; 
        } else {
            model.addAttribute("error", "Access Denied: Invalid Admin Credentials");
            return "admin-login";
        }
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

}
