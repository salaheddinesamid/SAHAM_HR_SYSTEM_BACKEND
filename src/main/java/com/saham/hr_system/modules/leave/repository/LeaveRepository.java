package com.saham.hr_system.modules.leave.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findAllByEmployee(Employee employee);
    Optional<Leave> findByReferenceNumber(String referenceNumber);
}
