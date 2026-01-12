package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

    @Query("SELECT AVG(a.totalDays) FROM Absence a")
    double getAbsenceAVG();
}
