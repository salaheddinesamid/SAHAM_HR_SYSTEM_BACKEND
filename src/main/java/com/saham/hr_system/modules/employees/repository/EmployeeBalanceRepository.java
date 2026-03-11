package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeBalanceRepository extends JpaRepository<EmployeeBalance, Long> {
    Optional<EmployeeBalance> findByEmployee(Employee employee);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT eb FROM EmployeeBalance eb")
    List<EmployeeBalance> findAllForUpdate();
}
