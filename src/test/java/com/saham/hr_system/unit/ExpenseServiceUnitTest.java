package com.saham.hr_system.unit;
/**
import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.expenses.dto.ExpenseResponseDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.repository.EmployeeRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseItemRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import com.saham.hr_system.modules.expenses.service.implementation.ExpenseServiceImpl;
import com.saham.hr_system.modules.expenses.service.implementation.processor.ExpenseRequestProcessor;
import com.saham.hr_system.modules.expenses.service.implementation.validator.ExpenseRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ExpenseItemRepository expenseItemRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseRequestProcessor expenseRequestProcessor;

    @Mock
    private ExpenseRequestValidator expenseRequestValidator;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@gmail.com");
    }

    @Test
    void testCreateExpenseSuccess() {
        // Arrange
        ExpenseRequestDto request = new ExpenseRequestDto();
        ExpenseItemRequest item1 = new ExpenseItemRequest();
        ExpenseItemRequest item2 = new ExpenseItemRequest();

        item1.setDesignation("Hotel");
        item1.setAmount(29000.0);

        item2.setDesignation("Transport");
        item2.setAmount(19000.0);

        request.setExpenseItems(List.of(item1, item2));
        request.setMotif("Business Trip");
        request.setIssueDate(LocalDate.of(2024, 12, 3));
        request.setBalance(2000);

        Expense processedExpense = new Expense();
        processedExpense.setTotalAmount(48000.0);

        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        doNothing().when(expenseRequestValidator).validateExpenseRequest(request);
        when(expenseRequestProcessor.processExpense(request, employee)).thenReturn(processedExpense);

        // Act
        ExpenseResponseDto response = expenseService.newExpense(employee.getEmail(), request);

        // Assert
        assertNotNull(response);
        assertEquals(48000.0, response.getTotalAmount());
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verify(expenseRequestValidator, times(1)).validateExpenseRequest(request);
        verify(expenseRequestProcessor, times(1)).processExpense(request, employee);
    }

    @Test
    void testCreateExpenseThrowEmployeeNotFound() {
        // Arrange
        ExpenseRequestDto request = new ExpenseRequestDto();
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
                expenseService.newExpense(employee.getEmail(), request)
        );
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verifyNoInteractions(expenseRequestValidator, expenseRequestProcessor);
    }

    @Test
    void testCreateExpenseThrowValidationException() {
        // Arrange
        ExpenseRequestDto request = new ExpenseRequestDto();
        request.setExpenseItems(List.of()); // Empty list to trigger validation exception

        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        doThrow(new IllegalArgumentException("Expense request must contain at least one expense item."))
                .when(expenseRequestValidator).validateExpenseRequest(request);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                expenseService.newExpense(employee.getEmail(), request)
        );
        assertEquals("Expense request must contain at least one expense item.", exception.getMessage());
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verify(expenseRequestValidator, times(1)).validateExpenseRequest(request);
        verifyNoInteractions(expenseRequestProcessor);
    }
}