package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Job;
import com.example.demo.JobRepository;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository repo;

    public List<Job> getAllJobs() {
        return repo.findAll();
    }

    public Job save(Job job) {
        return repo.save(job);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
