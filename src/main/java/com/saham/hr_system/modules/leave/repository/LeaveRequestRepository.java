package com.saham.hr_system.modules.leave.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    /**
     *
     * @param referenceNumber
     * @return
     */
    Optional<LeaveRequest> findByReferenceNumber(String referenceNumber);

    /**
     * This method returns all the requests made by subordinates of a manager that are in process and not yet approved by the manager.
     * @param employees
     * @param status
     * @param approvedByManager
     * @return list of leave requests
     */
    Page<LeaveRequest> findByEmployeeInAndStatusAndApprovedByManager(
            List<Employee> employees,
            LeaveRequestStatus status,
            boolean approvedByManager,
            Pageable pageable
    );

    /**
     *
     * @param approvedByManager
     * @param status
     * @param status1
     * @return
     */
    Page<LeaveRequest> findAllByApprovedByManagerOrStatusOrStatus(boolean approvedByManager, LeaveRequestStatus status,
                                                                  LeaveRequestStatus status1, Pageable pageable);

    /**
     *
     * @param start
     * @param end
     * @return
     */
    long countByRequestDateBetween(LocalDateTime start, LocalDateTime end);

    long countByEmployeeAndStatus(Employee employee, LeaveRequestStatus status);

    /**
     * Calculate the number of leave requests made by an employee
     * @param employee
     * @return
     */
    long countLeaveRequestByEmployee(Employee employee);

    /**
     *
     * @param employee
     * @return
     */
    Page<LeaveRequest> findAllByEmployee(Employee employee, Pageable pageable);

    Optional<LeaveRequest> findByEmployee(Employee employee);
}
