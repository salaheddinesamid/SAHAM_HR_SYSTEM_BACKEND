package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.EmployeeContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeContactDetailsRepository extends JpaRepository<EmployeeContactDetails, Long> {
}
