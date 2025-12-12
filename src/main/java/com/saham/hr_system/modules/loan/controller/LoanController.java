package com.saham.hr_system.modules.loan.controller;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.service.implementation.LoanApprovalImpl;
import com.saham.hr_system.modules.loan.service.implementation.LoanRequestQueryServiceImpl;
import com.saham.hr_system.modules.loan.service.implementation.LoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanServiceImpl loanService;
    private final LoanApprovalImpl loanApproval;
    private final LoanRequestQueryServiceImpl loanRequestQueryService;

    @Autowired
    public LoanController(LoanServiceImpl loanService, LoanApprovalImpl loanApproval, LoanRequestQueryServiceImpl loanRequestQueryService) {
        this.loanService = loanService;
        this.loanApproval = loanApproval;
        this.loanRequestQueryService = loanRequestQueryService;
    }

    @GetMapping("/requests/employee/get-all")
    public ResponseEntity<?> getAllEmployeeRequests(
            @RequestParam String email,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ){
        return
                ResponseEntity
                        .status(200)
                        .body(
                                loanRequestQueryService.getAllEmployeeRequests(email, pageNumber, pageSize)
                        );
    }

    @PostMapping("apply")
    public ResponseEntity<?> requestLoan(@RequestParam String email, @RequestBody LoanRequestDto requestDto) throws Exception {
        loanService.requestLoan(email, requestDto);
        return ResponseEntity
                .status(200)
                .body("Loan request submitted successfully");
    }
    @GetMapping("/requests/hr/get-all")
    public ResponseEntity<?> getAllRequests(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ){
        return ResponseEntity
                .status(200)
                .body(loanRequestQueryService.getAllRequests(
                        pageNumber, pageSize
                ));
    }
    @PutMapping("/requests/hr/approve-request")
    public ResponseEntity<?> approveLoanRequest(@RequestParam Long requestId) {
        // Implementation for approving loan request goes here
        loanApproval.approveLoanRequest(requestId);
        // HTTP response:
        return ResponseEntity
                .status(200)
                .body("Loan request approved successfully");
    }

    @PutMapping("/requests/hr/reject-request")
    public ResponseEntity<?> rejectLoanRequest(@RequestParam Long requestId) {
        // Implementation for rejecting loan request goes here
        loanApproval.rejectLoanRequest(requestId);
        // HTTP response:
        return ResponseEntity
                .status(200)
                .body("Loan request rejected successfully");
    }
}
