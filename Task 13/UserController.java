package com.example.demo;

import com.example.demo.UserEntity;
import com.example.demo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;

    // Constructor Injection
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    // 1. Create a User
    @PostMapping
    public ResponseEntity<UserEntity> addUser(@Valid @RequestBody UserEntity user) {
        UserEntity savedUser = repo.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // 2. Get All Users
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return repo.findAll();
    }

    // 3. Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Update User
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity userDetails) {
        return repo.findById(id).map(existingUser -> {
            // Update fields based on your UserEntity variable names
            existingUser.setName(userDetails.getName());
            existingUser.setMail(userDetails.getMail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            existingUser.setAddress(userDetails.getAddress());
            
            UserEntity updatedUser = repo.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok("User deleted with id = " + id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
