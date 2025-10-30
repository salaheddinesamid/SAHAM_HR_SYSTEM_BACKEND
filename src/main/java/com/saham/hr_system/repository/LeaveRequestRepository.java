package com.saham.hr_system.repository;

import com.saham.hr_system.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {
}
