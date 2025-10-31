package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findAllByEmployee(Employee employee);
}
