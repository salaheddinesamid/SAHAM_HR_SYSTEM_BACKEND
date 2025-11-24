package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRequestRepo extends JpaRepository<AbsenceRequest, Long> {
}
