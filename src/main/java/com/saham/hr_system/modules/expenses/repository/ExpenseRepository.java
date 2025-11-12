package com.saham.hr_system.modules.expenses.repository;

import com.saham.hr_system.modules.expenses.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
