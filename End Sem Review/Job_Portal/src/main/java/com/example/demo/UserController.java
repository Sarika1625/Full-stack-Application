package com.example.demo;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private ApplicationRepository appRepo;

    // 🔍 View + Filter Jobs
    @GetMapping("/jobs")
    public String jobs(@RequestParam(required = false) String location, Model model) {

        if (location != null && !location.isEmpty()) {
            model.addAttribute("jobs", jobRepo.findByLocationContaining(location));
        } else {
            model.addAttribute("jobs", jobRepo.findAll());
        }

        return "jobs";
    }
 // 🔹 Open Apply Page
    @GetMapping("/apply/{id}")
    public String openApplyPage(@PathVariable Long id, Model model) {

        Job job = jobRepo.findById(id).get();
        model.addAttribute("job", job);

        return "apply-job";
    }
    @GetMapping("/applications")
    public String viewApplications(@RequestParam(required = false) String email, Model model) {

        if (email != null && !email.isEmpty()) {
            List<JobApplication> apps = appRepo.findByEmail(email);
            model.addAttribute("applications", apps);
        }

        return "my-application";
    }
    
    @GetMapping("/myApplications")
    public String myApps(@RequestParam String email, Model model) {
        model.addAttribute("apps", appRepo.findByEmail(email));
        return "my-application";
    }

    // 📂 Apply with Resume Upload
    @PostMapping("/apply")
    public String apply(
            @RequestParam Long jobId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam("resume") MultipartFile file
    ) {

        try {
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = file.getOriginalFilename();
            String path = uploadDir + fileName;

            java.nio.file.Files.copy(
                    file.getInputStream(),
                    java.nio.file.Paths.get(path),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            JobApplication app = new JobApplication();
            app.setJobId(jobId);
            app.setFirstName(firstName);
            app.setLastName(lastName);
            app.setEmail(email);
            app.setPhone(phone);
            app.setResumePath(path);
            app.setStatus("APPLIED");

            appRepo.save(app);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";   // ✅ ONLY CHANGE
    }
    
}