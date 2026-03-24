package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{id}")
    public String getOrder(@PathVariable int id) {
        try {
            // Logic: Call User Service on 8281
            String user = restTemplate.getForObject(
                    "http://localhost:8282/users/" + id, 
                    String.class
            );
            return "Order ID: " + id + " placed by " + user;
        } catch (Exception e) {
            return "Error: User Service is not responding. Make sure Project 1 is running on port 8281.";
        }
    }
}