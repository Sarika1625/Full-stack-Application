package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // LIST ALL STUDENTS
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", service.getAll());
        return "students/list";
    }

    // SHOW CREATE FORM
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    // SAVE NEW STUDENT
    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            return "students/form";
        }

        service.create(student);
        return "redirect:/students";
    }

    // SHOW EDIT FORM
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", service.getById(id));
        return "students/form";
    }

    // UPDATE STUDENT
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            return "students/form";
        }

        service.update(id, student);
        return "redirect:/students";
    }

    // DELETE STUDENT
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/students";
    }
}