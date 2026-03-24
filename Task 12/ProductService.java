package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// REMOVED the broken edu.Task12 imports!
// Because Product and ProductRepository are in 'com.example.demo', 
// they are automatically available here.

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProduct(int id) {
        return repository.findById(id).orElse(null);
    }

    public Product getProductByName(String name) {
        // This will now use the findByName you created in the Repository
        return repository.findByName(name).orElse(null);
    }

    public Product getProductByCost(double cost) {
        return repository.findByCost(cost).orElse(null);
    }

    public void addData(Product product) {
        repository.save(product);
    }
}