package com.saham.hr_system.modules.loan.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest,Long> {

    List<LoanRequest> findAllByEmployee(Employee employee);

    /**
     * 
     * @param status
     * @return
     */
    List<LoanRequest> findAllByStatus(LoanRequestStatus status);
}
