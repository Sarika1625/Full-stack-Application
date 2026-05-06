package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private AdminRepository adminRepo;   // ✅ ADDED

    // 🔐 Admin Home (Protected)
    @GetMapping
    public String jobs(Model model, HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("jobs", jobRepo.findAll());
        return "admin";
    }

    // 🔐 Show Register Page
    @GetMapping("/register")
    public String registerPage() {
        return "admin-register";
    }

    // 🔐 Handle Register
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {

        if (adminRepo.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "admin-register";
        }

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        adminRepo.save(admin);

        return "redirect:/admin/login";
    }

    // 🔐 Show Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    // 🔐 Handle Login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Admin admin = adminRepo.findByUsername(username);

        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("admin", admin);
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "admin-login";
        }
    }

    // 🔐 Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ✅ Add Job
    @PostMapping("/addJob")
    public String addJob(@ModelAttribute Job job) {
        jobRepo.save(job);
        return "redirect:/admin";
    }

    // ✅ Delete Job
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        appRepo.deleteByJobId(id);
        jobRepo.deleteById(id);

        return "redirect:/admin";
    }

    // ✅ View Applications (Protected)
    @GetMapping("/applications")
    public String apps(Model model, HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("apps", appRepo.findAll());
        return "apply";
    }

    // ✅ Edit Job Page
    @GetMapping("/edit/{id}")
    public String editJob(@PathVariable Long id, Model model, HttpSession session) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }

        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("job", job);
        return "edit-job";
    }

    // ✅ Update Job
    @PostMapping("/updateJob")
    public String updateJob(@ModelAttribute Job job) {
        jobRepo.save(job);
        return "redirect:/admin";
    }

    // ✅ Shortlist / Reject Application
    @GetMapping("/status/{id}/{status}")
    public String status(@PathVariable Long id, @PathVariable String status) {

        JobApplication app = appRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(status);
        appRepo.save(app);

        return "redirect:/admin/applications";
    }
}