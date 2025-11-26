package com.saham.hr_system.modules.expenses.service.implementation.processor;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.dto.ExpenseItemRequest;
import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.expenses.model.Currency;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import com.saham.hr_system.modules.expenses.model.ExpenseLocation;
import com.saham.hr_system.modules.expenses.repository.ExpenseItemRepository;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import com.saham.hr_system.modules.expenses.utils.ExpenseUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpenseRequestProcessor {

    private final ExpenseRepository expenseRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final ExpenseUtils expenseUtils;

    public ExpenseRequestProcessor(ExpenseRepository expenseRepository, ExpenseItemRepository expenseItemRepository, ExpenseUtils expenseUtils) {
        this.expenseRepository = expenseRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.expenseUtils = expenseUtils;
    }

    @Transactional
    public Expense processExpense(ExpenseRequestDto expenseRequestDto, Employee employee) {

        Expense expense = new Expense();

        assert employee != null;
        expense.setEmployee(employee);
        expense.setIssueDate(expenseRequestDto.getIssueDate());
        expense.setCreatedAt(LocalDateTime.now());
        expense.setMotif(expenseRequestDto.getMotif());

        List<ExpenseItem> items = expenseRequestDto.getExpenseItems().stream()
                .map(req -> {
                    ExpenseItem item = new ExpenseItem();
                    item.setAmount(req.getAmount());
                    item.setDesignation(req.getDesignation());
                    item.setDate(req.getExpenseDate());
                    item.setExpense(expense);
                    return item;
                })
                .toList();

        expense.setItems(items); // set the items
        expense.setTotalAmount(expenseUtils.calculateTotalExpenseItems(items));
        expense.setCurrency(Currency.valueOf(expenseRequestDto.getCurrency())); // set the currency
        expense.setExchangeRate(expenseRequestDto.getExchangeRate()); // set the exchange rate
        expense.setExpenseLocation(ExpenseLocation.valueOf(expenseRequestDto.getLocation())); // set the location

        return expenseRepository.save(expense);

    }
}
