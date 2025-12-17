package com.saham.hr_system.leave.unit;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.model.RoleName;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.AnnualLeaveApproval;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnnualLeaveApprovalUnitTest {
    /**
     * Mocked objects
     * Employee
     * Manager
     * Leave request
     */
    private Employee employee;
    private EmployeeBalance employeeBalance;
    private Employee authorizedManager;
    private Employee unAuthorizedManager;
    private LeaveRequest leaveRequest;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveRepository leaveRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;
    @InjectMocks
    private AnnualLeaveApproval annualLeaveApproval;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        authorizedManager = new Employee();
        authorizedManager.setFirstName("Jane");
        authorizedManager.setLastName("Doe");
        authorizedManager.setEmail("jane.doe@saham.com");

        unAuthorizedManager = new Employee();
        unAuthorizedManager.setFirstName("Ronnie");
        unAuthorizedManager.setEmail("ronnie.doe@saham.com");

        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@saham.com");
        employee.setManager(authorizedManager);

        employeeBalance = new EmployeeBalance();
        employeeBalance.setBalanceId(1L);
        employeeBalance.setCurrentBalance(2);
        employeeBalance.setEmployee(employee);

        leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveRequestId(1L);
        leaveRequest.setReferenceNumber("LEAVEjohn.s202401010001");
        leaveRequest.setEmployee(employee);
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setApprovedByHr(false);
        leaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);
        leaveRequest.setStartDate(LocalDate.of(2025, 1, 1));
        leaveRequest.setEndDate(LocalDate.of(2025, 1, 2));
    }

    @Test
    void testApproveLeaveRequestBySupervisor(){
        // Arrange:
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.findByReferenceNumber("LEAVEjohn.s202401010001")).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        // Act:
        annualLeaveApproval.approveSubordinate(authorizedManager.getEmail(),leaveRequest);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testUnAuthorizedApproveAnnualLeaveRequestBySupervisor(){
        // Arrange:
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.findByReferenceNumber("LEAVEjohn.s202401010001")).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        // Act:
        annualLeaveApproval.approveSubordinate(authorizedManager.getEmail(),leaveRequest);
        assertThrows(UnauthorizedAccessException.class, ()->
            annualLeaveApproval.approveSubordinate(unAuthorizedManager.getEmail(),leaveRequest)
        );
    }

    @Test
    void testApproveLeaveRequestByHR(){
        String refNumber = "LEAVEjohn.s202401010001";

        // Arrange:
        when(leaveRequestRepository.findByReferenceNumber(refNumber)).thenReturn(Optional.of(leaveRequest));
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        // Act:
        annualLeaveApproval.approve(1L);
        // verify:
        verify(leaveRequestRepository, times(1)).save(any());
        verify(leaveRepository, times(1)).save(any());

    }

    @Test
    void testRejectLeaveRequestBySupervisor(){
        String refNumber = "LEAVEjohn.s202401010001";
        // Arrange:
        when(leaveRequestRepository.findByReferenceNumber(refNumber)).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        // act:
        annualLeaveApproval.rejectSubordinate(authorizedManager.getEmail(),leaveRequest);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testUnAuthorizedRejectAnnualLeaveRequestBySupervisor(){
        String refNumber = "LEAVEjohn.s202401010001";
        // Arrange:
        when(leaveRequestRepository.findByReferenceNumber(refNumber)).thenReturn(Optional.of(leaveRequest));
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        // act:
        assertThrows(UnauthorizedAccessException.class, ()->
                annualLeaveApproval.rejectSubordinate(unAuthorizedManager.getEmail(),leaveRequest));
    }

    @Test
    void testRejectLeaveRequestByHR(){}
}
