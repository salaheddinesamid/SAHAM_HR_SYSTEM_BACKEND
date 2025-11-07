package com.saham.hr_system.repository;

import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.Leave;
import com.saham.hr_system.model.LeaveRequest;
import com.saham.hr_system.model.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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

    List<LeaveRequest> findAllByEmployee(Employee employee);

    Optional<LeaveRequest> findByEmployee(Employee employee);
}
