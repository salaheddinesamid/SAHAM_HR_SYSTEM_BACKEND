package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    List<Employee> findAllByManagerId(Long id);

    List<Employee> findAllByRoles(List<Role> roles);
}
