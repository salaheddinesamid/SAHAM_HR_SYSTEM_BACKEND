package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {
}
