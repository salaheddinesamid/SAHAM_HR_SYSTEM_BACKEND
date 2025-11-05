package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findAllByEmployee(Employee employee);

    Optional<LeaveRequest> findByEmployee(Employee employee);
}
