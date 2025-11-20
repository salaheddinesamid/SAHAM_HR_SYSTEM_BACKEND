package com.saham.hr_system.modules.loan.repository;

import com.saham.hr_system.modules.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
