package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// REMOVED: import edu.Task12.Entity.Product; 
// You don't need to import Product because it is in the same package!

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Optional<Product> findByName(String name);
    
    // You can actually delete the findAll() below. 
    // JpaRepository already provides a working findAll() by default!
    List<Product> findAll();
    
    Optional<Product> findByCost(double cost);
}