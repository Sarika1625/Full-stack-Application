package edu.emp;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

	private Map<Integer, Employee> employeeMap = new HashMap<>();

    public void save(Employee emp) {
        employeeMap.put(emp.getId(), emp);
    }

    public Employee findById(int id) {
        return employeeMap.get(id);
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public boolean deleteById(int id) {
        return employeeMap.remove(id) != null;
    }
}