package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeBalanceRepository extends JpaRepository<EmployeeBalance, Long> {
    Optional<EmployeeBalance> findByEmployee(Employee employee);
}
