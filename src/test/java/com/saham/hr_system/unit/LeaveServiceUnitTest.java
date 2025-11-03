package com.saham.hr_system.unit;

import com.saham.hr_system.dto.LeaveRequestDto;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.EmployeeBalance;
import com.saham.hr_system.repository.EmployeeBalanceRepository;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LeaveRequestRepository;
import com.saham.hr_system.service.implementation.LeaveServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LeaveServiceUnitTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @InjectMocks
    private LeaveServiceImpl leaveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestLeave() {
        // Mock Employee Balance:
        EmployeeBalance balance = new EmployeeBalance();
        balance.setBalanceId(1L);
        balance.setDaysLeft(10);

        // Mock Employee:
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmail("test@test.com");

        // Mock Request DTO:
        LeaveRequestDto requestDto = new LeaveRequestDto(
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2024, 7, 5),
                "ANNUAL",
                ""
        );

        when(employeeRepository.findByEmail("test@test.com")).thenReturn(Optional.of(employee));
        when(employeeBalanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        when(leaveRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        leaveService.requestLeave(employee.getEmail(), requestDto);

        verify(leaveRequestRepository, times(1)).save(any());
    }
}
