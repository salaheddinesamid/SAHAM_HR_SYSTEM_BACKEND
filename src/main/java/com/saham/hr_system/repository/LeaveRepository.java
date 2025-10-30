package com.saham.hr_system.repository;

import com.saham.hr_system.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
}
