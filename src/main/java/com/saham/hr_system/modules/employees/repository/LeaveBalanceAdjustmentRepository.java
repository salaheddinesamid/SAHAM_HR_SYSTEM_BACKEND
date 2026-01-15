package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.LeaveBalanceAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveBalanceAdjustmentRepository extends JpaRepository<LeaveBalanceAdjustment, Long> {
}
