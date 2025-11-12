package com.saham.hr_system.modules.expenses.utils;

import com.saham.hr_system.modules.expenses.model.ExpenseItem;

import java.util.List;

public class ExpenseUtils {

    public double calculateTotalExpenseItems(List<ExpenseItem> items){
        return
                items.stream().map(ExpenseItem::getAmount).reduce(0.0, Double::sum);
    }
}
