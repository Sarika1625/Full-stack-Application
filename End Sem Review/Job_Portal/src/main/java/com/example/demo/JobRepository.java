package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;   // ✅ ADD THIS
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 🔍 Filter by location
    List<Job> findByLocationContaining(String location);

    @Query(value = "SELECT * FROM job LIMIT 10", nativeQuery = true)
    List<Job> findTopJobs();
}