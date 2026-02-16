package com.saham.hr_system.employee.unit;

import com.saham.hr_system.config.SecurityConfiguration;
import com.saham.hr_system.modules.employees.dto.PasswordUpdateDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeePasswordUpdateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
public class EmployeePasswordUpdateServiceUnitTest {

    @InjectMocks
    private EmployeePasswordUpdateServiceImpl employeePasswordUpdateService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    Employee employee;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setEmail("emp@saham.com");
        employee.setPassword(passwordEncoder.encode("emp2026@"));
    }

    @Test
    void testUpdateEmployeePasswordSuccess(){
        // Given:
        PasswordUpdateDto updateDto = new PasswordUpdateDto();
        updateDto.setOldPassword("emp2026@");
        updateDto.setNewPassword("emp2027@");
        // Arrange:
        when(employeeRepository.findByEmail("emp@saham.com")).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode("emp2026@")).thenReturn("encodedNewPassword");
        // Act and verify:
        employeePasswordUpdateService.updatePassword("emp@saham.com", updateDto);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void testUpdateEmployeePasswordThrowsEmployeeNotFound(){}

    @Test
    void testUpdateEmployeePasswordThrowsInvalidPassword() {}
}
