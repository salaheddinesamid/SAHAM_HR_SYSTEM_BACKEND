package com.saham.hr_system.modules.leave.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    /**
     * This method returns all the requests made by subordinates of a manager that are in process and not yet approved by the manager.
     * @param employee
     * @param status
     * @param approvedByManager
     * @return list of leave requests
     */
    List<LeaveRequest> findAllByEmployeeAndStatusAndApprovedByManager(Employee employee, LeaveRequestStatus status, boolean approvedByManager);

    List<LeaveRequest> findAllByApprovedByManagerOrStatusOrStatus(boolean approvedByManager, LeaveRequestStatus status, LeaveRequestStatus status1);

    long countByRequestDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Calculate the number of leave requests made by an employee
     * @param employee
     * @return
     */
    long countLeaveRequestByEmployee(Employee employee);
    List<LeaveRequest> findAllByEmployee(Employee employee);

    Optional<LeaveRequest> findByEmployee(Employee employee);
}
