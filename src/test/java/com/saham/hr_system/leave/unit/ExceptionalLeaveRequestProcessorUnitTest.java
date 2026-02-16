package com.saham.hr_system.leave.unit;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeBalance;
import com.saham.hr_system.modules.employees.repository.EmployeeBalanceRepository;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.leave.dto.LeaveRequestDto;
import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.service.implementation.ExceptionalLeaveRequestProcessor;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import com.saham.hr_system.utils.TotalDaysCalculator;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExceptionalLeaveRequestProcessorUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private TotalDaysCalculator  totalDaysCalculator;

    @Mock
    private LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    @InjectMocks
    private ExceptionalLeaveRequestProcessor exceptionalLeaveRequestProcessor;

    private Employee employee;
    private EmployeeBalance employeeBalance;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setEmail("salaheddine@saham.com");
        employee.setManager(null);

        employeeBalance = new EmployeeBalance();
        employeeBalance.setYear(2024);
        employeeBalance.setBalanceId(1L);
        employee.setEmployeeBalance(employeeBalance);
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
        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findByEmployee(employee)).thenReturn(Optional.of(employeeBalance));

        // Act:
        exceptionalLeaveRequestProcessor.process(employee.getEmail(),requestDto);
        verify(leaveRequestRepository, times(1)).save(any());
    }
}
