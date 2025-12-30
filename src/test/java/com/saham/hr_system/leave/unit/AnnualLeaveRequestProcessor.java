package com.saham.hr_system.leave.unit;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.model.LeaveType;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.DefaultLeaveRequestProcessor;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import com.saham.hr_system.utils.TotalDaysCalculator;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Service
public class AnnualLeaveRequestProcessor {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private TotalDaysCalculator totalDaysCalculator;

    @Mock
    private LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    @InjectMocks
    private DefaultLeaveRequestProcessor defaultLeaveRequestProcessor;

    private Employee employee;
    private EmployeeBalance employeeBalance;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Salaheddine");
        employee.setEmail("salaheddine@saham.com");

        employeeBalance = new EmployeeBalance();
        employeeBalance.setBalanceId(1L);
        employeeBalance.setAnnualBalance(30);
        employeeBalance.setYear(2025);
        employeeBalance.setCurrentBalance(1);
        employeeBalance.setEmployee(employee);
    }

    @Test
    void testProcessAnnualLeaveRequestSuccess() throws MessagingException {
        LeaveRequestDto requestDto = new LeaveRequestDto();
        requestDto.setStartDate(LocalDate.of(2024, 7, 1));
        requestDto.setEndDate(LocalDate.of(2024, 7, 5));
        requestDto.setType("ANNUAL");
        requestDto.setComment("");

        // Arrange:
        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        // Act:
        defaultLeaveRequestProcessor.process(employee.getEmail(),requestDto, null);
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
        verify(leaveRequestRepository, never()).save(any());

    }
}
