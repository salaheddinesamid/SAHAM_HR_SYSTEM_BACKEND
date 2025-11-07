package com.saham.hr_system.controller;

import com.saham.hr_system.dto.LoanRequestDto;
import com.saham.hr_system.service.implementation.LoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanServiceImpl loanService;

    @Autowired
    public LoanController(LoanServiceImpl loanService) {
        this.loanService = loanService;
    }

    @PostMapping("apply")
    public ResponseEntity<?> requestLoan(@RequestParam String email, @RequestBody LoanRequestDto requestDto) {
        loanService.requestLoan(email, requestDto);
        return ResponseEntity
                .status(200)
                .body("Loan request submitted successfully");
    }
}
