package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.BalanceAccrualHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceAccrualRepository extends JpaRepository<BalanceAccrualHistory, Long> {
}
