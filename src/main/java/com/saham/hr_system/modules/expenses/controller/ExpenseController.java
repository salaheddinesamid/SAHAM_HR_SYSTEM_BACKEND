package com.saham.hr_system.modules.expenses.controller;

import com.saham.hr_system.modules.expenses.dto.ExpenseRequestDto;
import com.saham.hr_system.modules.expenses.dto.ExpenseResponseDto;
import com.saham.hr_system.modules.expenses.service.implementation.ExpenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    @Autowired
    public ExpenseController(ExpenseServiceImpl expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("new")
    public ResponseEntity<?> createExpense(@RequestParam String email,
                                           @RequestBody ExpenseRequestDto expenseRequestDto) {

        // Act:
        ExpenseResponseDto response = expenseService.newExpense(email, expenseRequestDto);

        // return HTTP 200 with response body
        return ResponseEntity
                .status(200)
                .body(response);
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllExpenses(@RequestParam String email) {

        // Act:
        var response = expenseService.getAllExpenses(email);

        // return HTTP 200 with response body
        return ResponseEntity
                .status(200)
                .body(response);
    }
}
