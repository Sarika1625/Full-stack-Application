package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public String getUser(@PathVariable int id) {
        // This is what the Order Service will receive
        return "User ID: " + id + " Name: Sai";
    }
}