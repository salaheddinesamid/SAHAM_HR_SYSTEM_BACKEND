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

    @Autowired
    public ExpenseServiceImpl(EmployeeRepository employeeRepository, ExpenseRepository expenseRepository, ExpenseItemRepository expenseItemRepository) {
        this.employeeRepository = employeeRepository;
        this.expenseRepository = expenseRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponseDto newExpense(String email, ExpenseRequestDto expenseRequestDto) {
        // Fetch the employee:
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        if(expenseRequestDto.getExpenseItems() == null || expenseRequestDto.getExpenseItems().isEmpty()){
            throw new IllegalArgumentException("Expense must contain at least one item.");
        }

        // Otherwise:
        Expense expense = new Expense();
        expense.setEmployee(employee);
        expense.setIssueDate(expenseRequestDto.getIssueDate());

        // process items:
        List<ExpenseItem> items = processExpenseItems(expenseRequestDto.getExpenseItems());
        // If processing went successfully, save items to DB:
        expense.setItems(items);
        expense.setCreatedAt(LocalDateTime.now());

        assert items != null;
        double totalAmount= calculateTotalAmount(items);

        assert totalAmount > 0;
        expense.setTotalAmount(totalAmount);

        // save the new expense:
        Expense savedExpense =  expenseRepository.save(expense);
        return new ExpenseResponseDto(savedExpense);
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
