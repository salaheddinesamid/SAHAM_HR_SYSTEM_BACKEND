package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRequestRepo extends JpaRepository<AbsenceRequest, Long> {
    List<AbsenceRequest> findAllByEmployee(Employee employee);
}
