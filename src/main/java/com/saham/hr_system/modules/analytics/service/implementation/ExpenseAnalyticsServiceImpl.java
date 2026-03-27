package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.analytics.service.ExpenseAnalyticsService;
import com.saham.hr_system.modules.expenses.model.Expense;
import com.saham.hr_system.modules.expenses.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseAnalyticsServiceImpl implements ExpenseAnalyticsService {

    private final ExpenseRepository expenseRepository;
    @Override
    public List<Map<String, Double>> getYearlyOverview(String department, String entity, int year) {

        List<Map<String, Double>> analytics = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            Map<String, Double> data = fetchMonthlyData(department, entity, year, month);
            analytics.add(data);
        }
        return analytics;
    }

    /**
     * Fetches the monthly data for the given department, entity, year, and month.
     *
     * @param department The department for which to fetch the data.
     * @param entity The entity for which to fetch the data.
     * @param year The year for which to fetch the data.
     * @param month The month for which to fetch the data.
     * @return A map containing the analytics data for the month.
     */
    public Map<String, Double> fetchMonthlyData(String department, String entity, int year, int month) {

        // Mock a start and end date to fetch the data for the month
        LocalDate dateBefore =  LocalDate.of(year, month, 1);
        LocalDate dateAfter = dateBefore.plusMonths(1);

        List<Expense> expenses = expenseRepository.findAllByIssueDateBetween(
                dateBefore, dateAfter
        );

        double totalAmount = expenses.stream().map(Expense::getTotalAmount)
                .reduce(0.0,Double::sum);
        return Map.of(
                Month.of(month).toString(), totalAmount
        );
    }
}
