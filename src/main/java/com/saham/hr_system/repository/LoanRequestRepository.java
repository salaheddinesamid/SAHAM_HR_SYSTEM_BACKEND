package com.saham.hr_system.repository;

import com.saham.hr_system.model.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest,Long> {
}
