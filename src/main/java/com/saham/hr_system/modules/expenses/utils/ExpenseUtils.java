package com.saham.hr_system.modules.expenses.utils;

import com.saham.hr_system.modules.expenses.model.ExpenseItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpenseUtils {

    public double calculateTotalExpenseItems(List<ExpenseItem> items){
        return
                items.stream().map(ExpenseItem::getAmount).reduce(0.0, Double::sum);
    }
}
