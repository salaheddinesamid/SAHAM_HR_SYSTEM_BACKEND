package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    List<Employee> findAllByManagerId(Long id);
}
