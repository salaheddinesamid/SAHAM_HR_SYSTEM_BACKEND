package com.saham.hr_system.modules.expenses.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByEmployee(Employee employee);


    @Query("SELECT e FROM Expense e WHERE YEAR(e.issueDate) = :issueDateYear")
    List<Expense> findAllByIssueDateYear(int issueDateYear);

    List<Expense> findAllByIssueDateBetween(LocalDate issueDate, LocalDate issueDate2);
}
