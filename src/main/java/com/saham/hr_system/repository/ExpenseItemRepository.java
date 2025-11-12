package com.saham.hr_system.repository;

import com.saham.hr_system.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {
}
