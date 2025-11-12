package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeBalanceRepository extends JpaRepository<EmployeeBalance, Long> {
    Optional<EmployeeBalance> findByEmployee(Employee employee);
}
