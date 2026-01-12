package com.saham.hr_system.modules.absence.repo;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.employees.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AbsenceRequestRepo extends JpaRepository<AbsenceRequest, Long> {

    /**
     *
     * @param employee
     * @param pageable
     * @return
     */
    Page<AbsenceRequest> findAllByEmployee(Employee employee, Pageable pageable);
    /**
     *
     * @param employees
     * @return
     */
    Page<AbsenceRequest> findAllByEmployeeIn(List<Employee> employees, Pageable pageable);

    /**
     *
     * @param status
     * @param status2
     * @param approvedByManager
     * @param
     * @return
     */
    Page<AbsenceRequest> findAllByStatusOrStatusOrApprovedByManager(AbsenceRequestStatus status, AbsenceRequestStatus status2,
                                                                    boolean approvedByManager, Pageable pageable);

    /**
     * Count absence requests by employee
     * @param employee
     * @return
     */
    long countAbsenceRequestByEmployee(Employee employee);

    /**
     *
     * @param type
     * @param issueDateAfter
     * @param issueDateBefore
     * @return
     */
    long countByTypeAndIssueDateBetween(AbsenceType type, LocalDateTime issueDateAfter, LocalDateTime issueDateBefore);

    /**
     *
     * @param type
     * @return
     */
    long countByType(AbsenceType type);

    /**
     *
     * @param type
     * @param status
     * @param issueDateAfter
     * @param issueDateBefore
     * @return
     */
    long countByTypeAndStatusAndIssueDateBetween(AbsenceType type, AbsenceRequestStatus status,LocalDateTime issueDateAfter, LocalDateTime issueDateBefore);
    Optional<AbsenceRequest> findByReferenceNumber(String referenceNumber);
}
