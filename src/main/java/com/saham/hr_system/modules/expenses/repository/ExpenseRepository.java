package com.saham.hr_system.modules.expenses.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.expenses.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByEmployee(Employee employee);
}
