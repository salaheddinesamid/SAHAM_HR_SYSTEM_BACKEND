package com.saham.hr_system.modules.payroll.repository;

import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayrollHistoryRepository extends JpaRepository<PayrollHistory, Long> {
}
