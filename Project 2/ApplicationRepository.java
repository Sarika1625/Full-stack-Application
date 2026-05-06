package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByEmail(String email); 

    @Transactional
    @Modifying
    void deleteByJobId(Long jobId);   // ✅ FIXED ONLY THIS
}