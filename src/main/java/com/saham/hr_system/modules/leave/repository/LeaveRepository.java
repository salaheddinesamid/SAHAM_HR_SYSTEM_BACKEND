package com.saham.hr_system.modules.leave.repository;

import com.saham.hr_system.modules.leave.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
}
