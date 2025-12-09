package com.saham.hr_system.unit;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.leave.model.Leave;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.*;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import com.saham.hr_system.utils.TotalDaysCalculator;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LeaveServiceUnitTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Mock
    private LeaveRequestValidatorImpl leaveRequestValidator;

    @Mock
    private LeaveDocumentStorageServiceImpl leaveDocumentStorageService;

    @Mock
    private TotalDaysCalculator totalDaysCalculator;

    @Mock
    private LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    @InjectMocks
    private AnnualLeaveApproval annualLeaveApproval;

    @InjectMocks
    private DefaultLeaveRequestProcessor defaultLeaveRequestProcessor;

    @InjectMocks
    private ExceptionalLeaveRequestProcessor exceptionalLeaveRequestProcessor;

    @InjectMocks
    private LeaveRequestCancelerImpl leaveRequestCanceler;


    private Employee employee;
    private Employee subordinate;
    private Employee manager2;
    private EmployeeBalance subordinateBalance;
    private EmployeeBalance employeeBalance;
    private LeaveRequest leaveRequest;
    private LeaveRequest subordinateLeaveRequest;
    private Leave subordinateLeave;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Ciryane");
        employee.setEmail("Ciryane@saham.com");

        subordinate = new Employee();
        subordinate.setId(2L);
        subordinate.setFirstName("Salaheddine");
        subordinate.setLastName("Samid");
        subordinate.setEmail("salaheddine@saham.com");
        subordinate.setManager(employee);

        manager2 = new Employee();
        manager2.setId(3L);
        manager2.setFirstName("Manager2");
        manager2.setLastName("Manager");
        manager2.setEmail("manager2@saham.com");
        manager2.setManager(null);

        employeeBalance = new EmployeeBalance();
        employeeBalance.setBalanceId(1L);
        employeeBalance.setAnnualBalance(30);
        employeeBalance.setYear(2025);
        employeeBalance.setCurrentBalance(1);
        employeeBalance.setEmployee(employee);

        subordinateBalance = new EmployeeBalance();
        subordinateBalance.setBalanceId(1L);
        subordinateBalance.setAnnualBalance(30);
        subordinateBalance.setYear(2025);
        subordinateBalance.setCurrentBalance(20);
        subordinateBalance.setEmployee(employee);

        // Leave request made by manager:
        leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveRequestId(1L);
        leaveRequest.setStartDate(LocalDate.of(2024, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2024, 7, 5));
        leaveRequest.setApprovedByManager(false);
        leaveRequest.setTypeOfLeave(LeaveType.ANNUAL);
        leaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);
        leaveRequest.setApprovedByHr(false);
        leaveRequest.setEmployee(employee);

        // Leave request made by subordinate:
        subordinateLeaveRequest = new LeaveRequest();
        subordinateLeaveRequest.setLeaveRequestId(2L);
        subordinateLeaveRequest.setStartDate(LocalDate.of(2024, 7, 1));
        subordinateLeaveRequest.setEndDate(LocalDate.of(2024, 7, 5));
        subordinateLeaveRequest.setApprovedByManager(false);
        subordinateLeaveRequest.setApprovedByHr(false);
        subordinateLeaveRequest.setStatus(LeaveRequestStatus.IN_PROCESS);
        subordinateLeaveRequest.setEmployee(subordinate);

        subordinateLeave = new Leave();
        subordinateLeave.setLeaveId(2L);
        subordinateLeave.setFromDate(LocalDate.of(2024, 7, 1));
        subordinateLeave.setToDate(LocalDate.of(2024, 7, 5));
        subordinateLeave.setLeaveType(LeaveType.ANNUAL);
        subordinateLeave.setTotalDays(4);
        subordinateLeave.setEmployee(subordinate);

    }

    /**
     *
     * @throws MessagingException
     */
    @Test
    void testProcessAnnualLeaveRequestSuccess() throws MessagingException {
        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 7, 1));
        requestDto.setEndDate(LocalDate.of(2024, 7, 5));
        requestDto.setType("ANNUAL");
        requestDto.setComment("");

        // Arrange:
        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(subordinate));
        when(employeeBalanceRepository.findByEmployee(subordinate)).thenReturn(Optional.of(subordinateBalance));

        // Act:
        defaultLeaveRequestProcessor.process(subordinate.getEmail(),requestDto, null);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testProcessExceptionalLeaveRequestSuccess() throws MessagingException, IOException {
        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 7, 1));
        requestDto.setEndDate(LocalDate.of(2024, 7, 5));
        requestDto.setType("EXCEPTIONAL");
        requestDto.setTypeDetails("SICKNESS");
        requestDto.setComment("");

        // Arrange:
        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(subordinate));
        when(employeeBalanceRepository.findByEmployee(subordinate)).thenReturn(Optional.of(subordinateBalance));

        // Act:
        exceptionalLeaveRequestProcessor.process(subordinate.getEmail(),requestDto, null);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testProcessAnnualLeaveRequestShouldThrowEmployeeNotFound(){
        // Mock request:
        LeaveRequestDto leaveRequestDto = new LeaveRequestDto();
        leaveRequestDto.setType(LeaveType.ANNUAL.toString());
        leaveRequestDto.setStartDate(LocalDate.of(2024, 7, 1));
        leaveRequestDto.setEndDate(LocalDate.of(2024, 7, 5));

        // Arrange:
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        // Act:
        assertThrows(UserNotFoundException.class, ()->
                defaultLeaveRequestProcessor.process("test@example.com", leaveRequestDto, null));

    }

    @Test
    void testApproveAnnualLeaveRequestBySupervisor(){
        Long requestId = 2L;
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        // Act:
        annualLeaveApproval.approveSubordinate("Ciryane@saham.com",subordinateLeaveRequest);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testApproveAnnualLeaveRequestThrowSecurityException(){
        Long requestId = 2L;
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        // Act and verify:
        assertThrows(UnauthorizedAccessException.class, ()-> annualLeaveApproval.approveSubordinate("manager2@saham.com",subordinateLeaveRequest));
    }

    @Test
    void testApproveAnnualLeaveRequest(){
        Long requestId = 2L;
        subordinateLeaveRequest.setApprovedByManager(true);
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        when(employeeRepository.findByEmail(subordinate.getEmail())).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(subordinate)).thenReturn(Optional.of(subordinateBalance));
        // Act:
        annualLeaveApproval.approve(requestId);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testRejectAnnualSubordinateLeaveRequestSuccess(){
        Long requestId = 2L;
        String managerEmail = "Ciryane@saham.com";
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        // Act:
        annualLeaveApproval.rejectSubordinate(managerEmail,subordinateLeaveRequest);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testRejectAnnualSubordinateLeaveRequestThrowsSecurityException(){
        Long requestId = 2L;
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        // Act:
        assertThrows(UnauthorizedAccessException.class, ()-> annualLeaveApproval.rejectSubordinate("manager2@saham.com",subordinateLeaveRequest));
    }

    @Test
    void shouldThrowRequestNotApprovedBySupervisor(){
        Long requestId = 1L;
        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        // Act
        assertThrows(LeaveRequestNotApprovedBySupervisorException.class,
                ()-> annualLeaveApproval.approve(requestId));
    }

    @Test
    void testApproveLeaveRequest(){
        Long requestId = 2L;
        subordinateLeaveRequest.setApprovedByManager(true);
        // Arrange:
        when(leaveRequestRepository.findById(2L)).thenReturn(Optional.of(subordinateLeaveRequest));
        when(employeeRepository.findByEmail(subordinate.getEmail())).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(subordinate)).thenReturn(Optional.of(subordinateBalance));
        // Act:
        annualLeaveApproval.approve(requestId);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void testCancelLeaveRequestSuccess(){
        Long requestId = 2L;
        String refNumber = "SHDC-SAHAMEMP20241202-0001";
        // Arrange:
        when(leaveRequestRepository.findByReferenceNumber(refNumber)).thenReturn(Optional.of(subordinateLeaveRequest));
        // Act:
        leaveRequestCanceler.cancel(refNumber);
        // verify:
        verify(leaveRequestRepository, times(1)).save(any());
    }

    /* To be fixed (cancel leave by ref N°)
    @Test
    void testCancelLeaveSuccess(){
        Long id = 2L;
        // Arrange:
        when(leaveRepository.findById(id)).thenReturn(Optional.of(subordinateLeave));
        when(employeeBalanceRepository.findByEmployee(subordinate)).thenReturn(Optional.of(subordinateBalance));
        subordinateBalance.setDaysLeft(15);
        subordinateBalance.setUsedBalance(10);

        // Act:
        leaveCanceller.cancel(id);

        // verify and assert:
        verify(leaveRepository, times(1)).delete(any());
        assertEquals(19, subordinateBalance.getDaysLeft());
        assertEquals(6, subordinateBalance.getUsedBalance());
    }

     */
}
