package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest,Long> {

    List<LoanRequest> findAllByEmployee(Employee employee);
}
