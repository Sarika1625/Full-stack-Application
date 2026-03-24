
package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitRepository repo;

    public FruitController(FruitRepository repo) {
        this.repo = repo;
    }

    // Home URL (optional)
    @GetMapping("/")
    public String home() {
        return "Fruit API is running";
    }

    // CREATE
    @PostMapping
    public Fruit addFruit(@RequestBody Fruit fruit) {
        return repo.save(fruit);
    }

    // READ ALL
    @GetMapping
    public List<Fruit> getAllFruits() {
        return repo.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Fruit getFruit(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Fruit updateFruit(@PathVariable Long id, @RequestBody Fruit fruit) {

        Fruit existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(fruit.getName());
            existing.setQuantity(fruit.getQuantity());
            return repo.save(existing);
        }

        return null;
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteFruit(@PathVariable Long id) {
        repo.deleteById(id);
        return "Deleted fruit id = " + id;
    }

    // COUNT
    @GetMapping("/count")
    public long countFruits() {
        return repo.count();
    }
}



