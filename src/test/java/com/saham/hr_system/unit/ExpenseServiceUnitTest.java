package com.saham.hr_system.modules.expenses.service.implementation;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.expenses.dto.ExpenseResponseDto;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import com.saham.hr_system.modules.expenses.repository.ExpenseItemRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
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

class ExpenseServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseItemRepository expenseItemRepository;

    @Mock
    private ExpenseRequestValidator expenseRequestValidator;

    @Mock
    private ExpenseRequestProcessor expenseRequestProcessor;

    @InjectMocks
    private ExpenseServiceImpl expenseService;  // the class under test

    private Employee employee;
    private ExpenseRequestDto requestDto;
    private Expense processedExpense;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@gmail.com");

        ExpenseItemRequest item1 = new ExpenseItemRequest();
        item1.setDesignation("Hotel");
        item1.setAmount(2000.0);

        ExpenseItemRequest item2 = new ExpenseItemRequest();
        item2.setDesignation("Transport");
        item2.setAmount(1000.0);

        requestDto = new ExpenseRequestDto();
        requestDto.setIssueDate(LocalDate.of(2024, 12, 3));
        requestDto.setExpenseItems(List.of(item1, item2));

        ExpenseItem expenseItem1 = new ExpenseItem();
        expenseItem1.setDesignation("Hotel");
        expenseItem1.setAmount(2000.0);

        ExpenseItem expenseItem2 = new ExpenseItem();
        expenseItem2.setDesignation("Transport");
        expenseItem2.setAmount(1000.0);

        processedExpense = new Expense();
        processedExpense.setEmployee(employee);
        processedExpense.setTotalAmount(3000.0);
        processedExpense.setItems(List.of(expenseItem1, expenseItem2));
    }

    @Test
    void testNewExpense_Success() {
        // Arrange
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        doNothing().when(expenseRequestValidator).validateExpenseRequest(requestDto);
        when(expenseRequestProcessor.processExpense(requestDto, employee)).thenReturn(processedExpense);

        // Act
        ExpenseResponseDto response = expenseService.newExpense(employee.getEmail(), requestDto);

        // Assert
        assertNotNull(response);
        assertEquals("John Smith",response.getIssueBy());
        verify(employeeRepository, times(1)).findByEmail(employee.getEmail());
        verify(expenseRequestValidator, times(1)).validateExpenseRequest(requestDto);
        verify(expenseRequestProcessor, times(1)).processExpense(requestDto, employee);
    }

    @Test
    void testNewExpense_EmployeeNotFound_ThrowsException() {
        // Arrange
        when(employeeRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
                expenseService.newExpense("unknown@gmail.com", requestDto));

        verify(expenseRequestValidator, never()).validateExpenseRequest(any());
        verify(expenseRequestProcessor, never()).processExpense(any(), any());
    }

    @Test
    void testNewExpense_ValidationFails_ThrowsException() {
        // Arrange
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));
        doThrow(new IllegalArgumentException("Expense request must contain at least one expense item."))
                .when(expenseRequestValidator).validateExpenseRequest(requestDto);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                expenseService.newExpense(employee.getEmail(), requestDto));

        assertEquals("Expense request must contain at least one expense item.", thrown.getMessage());
        verify(expenseRequestProcessor, never()).processExpense(any(), any());
    }
}
