package com.saham.hr_system.modules.loan.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest,Long> {

    /**
     *
     */

    @NotNull Page<LoanRequest> findAll(@NotNull Pageable pageable);
    /**
     *
     * @param employee
     * @return
     */
    Page<LoanRequest> findAllByEmployee(Employee employee, Pageable pageable);

    @Query("SELECT l FROM LoanRequest l WHERE YEAR(l.issueDate) = :issueDateYear")
    List<LoanRequest> findAllByIssueDateYear(int issueDateYear);

    Optional<LoanRequest> findByStatus(LoanRequestStatus status);

    /**
     *
     * @param employee
     * @return the number of loan requests made by an employee.
     */
    long countByEmployee(Employee employee);
}
