package com.example.demo;

import java.util.List;

public interface StudentService {

    List<Student> getAll();

    Student getById(Long id);

    Student create(Student student);

    Student update(Long id, Student student);

    void delete(Long id);
}