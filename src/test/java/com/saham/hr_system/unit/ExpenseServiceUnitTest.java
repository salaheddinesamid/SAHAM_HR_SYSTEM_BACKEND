package com.saham.hr_system.unit;

import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseItemRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import com.saham.hr_system.modules.expenses.service.implementation.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ExpenseServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ExpenseItemRepository expenseItemRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    private Employee employee;

    @InjectMocks
    private ExpenseServiceImpl expenseService;



    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@gmail.com");
    }


    @Test
    void testCalculateTotalAmount(){
        // Create expense items:
        ExpenseItem item1 = new ExpenseItem();
        ExpenseItem item2 = new ExpenseItem();

        item1.setDesignation("Hotel");
        item1.setAmount(29000.0);

        item2.setDesignation("Transport");
        item2.setAmount(19000.0);

        // Calculate total:
        double total = expenseService.calculateTotalAmount(
                List.of(item1, item2)
        );

        // Verify:
        assert(total == 48000.0);
    }
    @Test
    void testCreateExpenseSuccess(){
        // Create new request:
        ExpenseRequestDto request = new ExpenseRequestDto();
        ExpenseItemRequest item1 = new ExpenseItemRequest();
        ExpenseItemRequest item2 = new ExpenseItemRequest();

        item1.setDesignation("");
        item1.setAmount(29000.0);

        item2.setDesignation("");
        item2.setAmount(19000.0);
        request.setExpenseItems(
                List.of(item1, item2)
        );
        request.setMotif("Business Trip");
        request.setIssueDate(LocalDate.of(2024,12,3));
        request.setBalance(2000);

        // Arrange:
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        // Act:
        expenseService.newExpense(employee.getEmail(), request);

        // Verify that service reaches the repository to save the expense:
        verify(expenseRepository, times(1)).save(any());
    }

    @Test
    void testCreateExpenseThrowEmployeeNotFound(){}
}
