package com.saham.hr_system.modules.expenses.repository;

import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {
}
