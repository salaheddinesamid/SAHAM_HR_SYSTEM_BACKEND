package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.analytics.dto.ExpenseAnalyticsDto;
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

    /**
     * Retrieves the yearly overview of expenses for a given department, entity, and year.
     *
     * @param department The department for which to retrieve the overview.
     * @param entity The entity for which to retrieve the overview.
     * @param year The year for which to retrieve the overview.
     * @return A list of maps containing the monthly analytics data for the specified parameters.
     */
    @Override
    public List<ExpenseAnalyticsDto> getYearlyOverview(String department, String entity, int year) {

        /*


        // Iterate over each month in the year
        for (int month = 1; month <= 12; month++) {
            Map<String, Double> data = fetchMonthlyData(department, entity, year, month);
            analytics.add(data);
            ExpenseAnalyticsDto dto = new ExpenseAnalyticsDto();
        }
        return analytics;

         */
        // Initialize the list to hold the analytics data for each month
        List<ExpenseAnalyticsDto> analytics = new ArrayList<>();
        // Mock a start and end date to fetch the data for the month
        for (int month = 1; month <= 12; month++) {
            LocalDate dateBefore =  LocalDate.of(year, month, 1);
            LocalDate dateAfter = dateBefore.plusMonths(1);

            List<Expense> expenses = expenseRepository.findAllByIssueDateBetween(
                    dateBefore, dateAfter
            );

            double totalAmount = expenses.stream().map(Expense::getTotalAmount)
                    .reduce(0.0,Double::sum);
            long totalNumberOfExpenses = expenses.size();

            ExpenseAnalyticsDto dto = new ExpenseAnalyticsDto(
                    totalNumberOfExpenses,
                    totalAmount,
                    Month.of(month).toString()
            );
            analytics.add(dto);
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
        return null;
    }

}
