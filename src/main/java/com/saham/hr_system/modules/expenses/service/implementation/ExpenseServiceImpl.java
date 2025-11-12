package com.saham.hr_system.modules.expenses.service.implementation;

import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.expenses.dto.ExpenseResponseDto;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseItemRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import com.saham.hr_system.modules.expenses.service.ExpenseService;
import com.saham.hr_system.modules.expenses.service.implementation.processor.ExpenseRequestProcessor;
import com.saham.hr_system.modules.expenses.service.implementation.validator.ExpenseRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final EmployeeRepository employeeRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final ExpenseRequestValidator expenseRequestValidator;
    private final ExpenseRequestProcessor expenseRequestProcessor;

    @Autowired
    public ExpenseServiceImpl(EmployeeRepository employeeRepository, ExpenseRepository expenseRepository, ExpenseItemRepository expenseItemRepository, ExpenseRequestValidator expenseRequestValidator, ExpenseRequestProcessor expenseRequestProcessor) {
        this.employeeRepository = employeeRepository;
        this.expenseRepository = expenseRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.expenseRequestValidator = expenseRequestValidator;
        this.expenseRequestProcessor = expenseRequestProcessor;
    }

    @Override
    @Transactional
    public ExpenseResponseDto newExpense(String email, ExpenseRequestDto expenseRequestDto) {
        // Fetch the employee:
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        // Validate the request dto:
        expenseRequestValidator.validateExpenseRequest(expenseRequestDto);

        // Otherwise:
        // process the expense request:
        Expense processedExpense = expenseRequestProcessor.processExpense(expenseRequestDto, employee);
        return new ExpenseResponseDto(processedExpense);
    }

    // This function process each expense item and save it to the database
    private List<ExpenseItem> processExpenseItems(List<ExpenseItemRequest> itemRequests){
        return itemRequests.stream()
                .map(request -> {
                    ExpenseItem item = new ExpenseItem();
                    item.setAmount(request.getAmount());
                    item.setDate(request.getExpenseDate());
                    item.setDesignation(request.getDesignation());
                    // save the expense item:
                    return expenseItemRepository.save(item);
                }).toList();
    }

    // This function takes a list of all expense items and returns the total amount
    public double calculateTotalAmount(List<ExpenseItem> items){
        return
                items.stream().map(ExpenseItem::getAmount).reduce(0.0, Double::sum);
    }
}
