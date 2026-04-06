package com.saham.hr_system.analytics.unit;

import com.saham.hr_system.modules.analytics.service.implementation.ExpenseAnalyticsServiceImpl;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class ExpenseAnalyticsUnitTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseAnalyticsServiceImpl expenseAnalyticsService;

    Expense ex1;
    Expense ex2;
    Expense ex3;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // Mock Expenses:
        ex1 = new Expense();
        ex1.setEmployee(null);
        ex1.setTotalAmount(2000);
        ex1.setIssueDate(LocalDate.of(2026, 1, 15));

        ex2 = new Expense();
        ex2.setEmployee(null);
        ex2.setTotalAmount(2000);
        ex2.setIssueDate(LocalDate.of(2026, 1, 19));

        ex3 = new Expense();
        ex3.setEmployee(null);
        ex3.setTotalAmount(2000);
        ex3.setIssueDate(LocalDate.of(2026, 1, 23));
    }

    @Test
    void testGetExpenseYearlyOverviewSuccess(){
        // Arrange
        when(expenseRepository.findAllByIssueDateBetween(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1)))
                .thenReturn(List.of(ex1, ex2, ex3));
        // Act and verify
        Map<String, Double> result1 = expenseAnalyticsService.fetchMonthlyData
                ("","", 2026, 1);
        Map<String, Double> result2 = expenseAnalyticsService.fetchMonthlyData("","", 2026, 2);
    }

}
