package com.saham.hr_system.unit;

import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.exception.InsufficientBalanceException;
import com.saham.hr_system.exception.LeaveRequestNotApprovedBySupervisorException;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.leave.model.LeaveRequestStatus;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.DefaultLeaveRequestProcessor;
import com.saham.hr_system.modules.leave.service.implementation.ExceptionalLeaveRequestProcessor;
import com.saham.hr_system.modules.leave.service.implementation.LeaveRequestValidatorImpl;
import com.saham.hr_system.modules.leave.service.implementation.LeaveServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LeaveServiceUnitTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Mock
    private LeaveRequestValidatorImpl leaveRequestValidator;

    @Mock
    private DefaultLeaveRequestProcessor defaultLeaveRequestProcessor;

    @Mock
    private ExceptionalLeaveRequestProcessor exceptionalLeaveRequestProcessor;


    private Employee employee;
    private Employee subordinate;
    private EmployeeBalance subordinateBalance;
    private EmployeeBalance employeeBalance;
    private LeaveRequest leaveRequest;
    private LeaveRequest subordinateLeaveRequest;

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

        employeeBalance = new EmployeeBalance();
        employeeBalance.setBalanceId(1L);
        employeeBalance.setInitialBalance(30);
        employeeBalance.setYear(2025);
        employeeBalance.setDaysLeft(1);
        employeeBalance.setEmployee(employee);

        subordinateBalance = new EmployeeBalance();
        subordinateBalance.setBalanceId(1L);
        subordinateBalance.setInitialBalance(30);
        subordinateBalance.setYear(2025);
        subordinateBalance.setDaysLeft(1);
        subordinateBalance.setEmployee(employee);

        // Leave request made by manager:
        leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveRequestId(1L);
        leaveRequest.setStartDate(LocalDate.of(2024, 7, 1));
        leaveRequest.setEndDate(LocalDate.of(2024, 7, 5));
        leaveRequest.setApprovedByManager(true);
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

    }

    // To be rewritten due to code changes:
    @Test
    void testRequestLeave() throws IOException {

        // Mock Request DTO:
        LeaveRequestDto requestDto = new LeaveRequestDto(
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 5),
                "ANNUAL",
                "",
                ""
        );

        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        when(leaveRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        leaveService.requestLeave(employee.getEmail(), requestDto, file);

        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowIfInsufficientBalance() {

        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 7, 1));
        requestDto.setEndDate(LocalDate.of(2024, 7, 5));
        // Arrange
        employeeBalance.setDaysLeft(0);
        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class,
                () -> leaveService.requestLeave("Ciryane@saham.com", requestDto, null));
        verify(leaveRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowEmployeeNotFound(){
        // Mock request:
        LeaveRequestDto leaveRequestDto = new LeaveRequestDto();
        leaveRequestDto.setStartDate(LocalDate.of(2024, 7, 1));
        leaveRequestDto.setEndDate(LocalDate.of(2024, 7, 5));

        // Arrange:
        when(employeeRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        // Act:
        assertThrows(UserNotFoundException.class, ()-> leaveService.requestLeave("test@example.com", leaveRequestDto, null));

    }

    @Test
    void testApproveLeaveRequestBySupervisor(){
        Long requestId = 1L;

        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        when(leaveRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act:
        leaveService.approveSubordinateLeaveRequest(requestId);
        verify(leaveRequestRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowRequestNotApprovedBySupervisor(){
        Long requestId = 1L;
        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));
        when(leaveRequestRepository.findById(1L)).thenReturn(Optional.of(leaveRequest));

        // Act
        assertThrows(LeaveRequestNotApprovedBySupervisorException.class,
                ()-> leaveService.approveLeaveRequest(requestId));
    }

    @Test
    void testApproveLeaveRequest(){

    }

    @Test
    void testGetAllSubordinatesLeaveRequestShouldReturnOnlyInProcessAndNotApprovedByManager() {
        // Arrange:
        when(employeeRepository.findByEmail("Ciryane@saham.com")).thenReturn(Optional.of(employee));
        when(employeeRepository.findAllByManagerId(employee.getId())).thenReturn(List.of(subordinate));
        when(leaveRequestRepository.findAllByEmployeeAndStatusAndApprovedByManager(
                subordinate, LeaveRequestStatus.IN_PROCESS, false))
                .thenReturn(List.of(subordinateLeaveRequest));

        // Act:
        var result = leaveService.getAllSubordinatesRequests(employee.getEmail());

        // Assert:
        assertEquals(1, result.size());
        verify(leaveRequestRepository, times(1))
                .findAllByEmployeeAndStatusAndApprovedByManager(subordinate, LeaveRequestStatus.IN_PROCESS, false);
    }
}
