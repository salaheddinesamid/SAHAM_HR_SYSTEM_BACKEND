package com.saham.hr_system.modules.loan.controller;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.service.implementation.LoanServiceImpl;
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

    @GetMapping("get-all-requests")
    public ResponseEntity<?> getAllRequests(){
        return ResponseEntity
                .status(200)
                .body(loanService.getAllLoanRequests());
    }

    @GetMapping("/employee-requests")
    public ResponseEntity<?> getAllEmployeeRequests(@RequestParam String email){
        return
                ResponseEntity
                        .status(200)
                        .body(loanService.getAllEmployeeRequests(email));
    }

    @PostMapping("apply")
    public ResponseEntity<?> requestLoan(@RequestParam String email, @RequestBody LoanRequestDto requestDto) throws Exception {
        loanService.requestLoan(email, requestDto);
        return ResponseEntity
                .status(200)
                .body("Loan request submitted successfully");
    }
}
