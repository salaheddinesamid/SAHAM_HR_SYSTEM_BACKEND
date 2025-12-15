package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbsenceRequestRepo extends JpaRepository<AbsenceRequest, Long> {
    /**
     *
     * @param employee
     * @return
     */
    List<AbsenceRequest> findAllByEmployee(Employee employee, Pageable pageable);

    /**
     *
     * @param status
     * @param status2
     * @param approvedByManager
     * @param sort
     * @return
     */
    List<AbsenceRequest> findAllByStatusOrStatusOrApprovedByManager(AbsenceRequestStatus status, AbsenceRequestStatus status2,
                                                                    boolean approvedByManager, Pageable pageable);

    /**
     * Count absence requests by employee
     * @param employee
     * @return
     */
    long countAbsenceRequestByEmployee(Employee employee);

    Optional<AbsenceRequest> findByReferenceNumber(String referenceNumber);
}
