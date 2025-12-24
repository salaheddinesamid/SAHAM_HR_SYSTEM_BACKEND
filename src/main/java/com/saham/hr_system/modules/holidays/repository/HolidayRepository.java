package com.saham.hr_system.modules.holidays.repository;

import com.saham.hr_system.modules.holidays.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    /**
     *
     * @param name
     * @return
     */
    boolean existsByName(String name);

    /**
     *
     * @param name
     * @return
     */
    Optional<Holiday> findByName(String name);
}
