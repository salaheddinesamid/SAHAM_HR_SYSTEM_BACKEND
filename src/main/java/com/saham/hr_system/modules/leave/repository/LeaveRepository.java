package com.saham.hr_system.modules.leave.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    /**
     * Fetch all the leaves of an employee.
     * @param employee: employee Id
     * @return a list of the employee leaves.
     */
    List<Leave> findAllByEmployee(Employee employee);

    /**
     * Fetch a leave by a Ref N° from the database.
     * @param referenceNumber: the ref N° of the leave
     * @return the existing leave.
     */
    Optional<Leave> findByReferenceNumber(String referenceNumber);

    /**
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    List<Leave> findAllByFromDateOrToDateIs(LocalDate fromDate, LocalDate toDate);
}
