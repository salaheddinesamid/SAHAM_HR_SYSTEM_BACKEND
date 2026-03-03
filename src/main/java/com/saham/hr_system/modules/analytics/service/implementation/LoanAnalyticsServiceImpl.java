package com.saham.hr_system.modules.analytics.service.implementation;

import com.saham.hr_system.modules.analytics.dto.LoanAnalyticsDto;
import com.saham.hr_system.modules.analytics.service.LoanAnalyticsService;
import com.saham.hr_system.modules.leave.model.LeaveRequest;
import com.saham.hr_system.modules.loan.model.Loan;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanAnalyticsServiceImpl implements LoanAnalyticsService {

    private final LoanRequestRepository loanRequestRepository;

    @Autowired
    public LoanAnalyticsServiceImpl(LoanRequestRepository loanRequestRepository) {
        this.loanRequestRepository = loanRequestRepository;
    }

    @Override
    public LoanAnalyticsDto getLoanAnalyticsOverview(String type, LocalDate from, LocalDate to, String department, String entity) {
        List<LoanRequest> loanRequests = loanRequestRepository.findAll();
        List<LoanRequest> filteredLoanRequests = new ArrayList<>();


        long totalLoanRequests = 0;
        long totalApprovedLoanRequests = 0;
        long totalRejectedLoanRequests = 0;
        long totalAmountApproved = 0;
        long totalAmountRejected = 0;

        return new LoanAnalyticsDto(
                totalLoanRequests,
                totalApprovedLoanRequests,
                totalRejectedLoanRequests,
                totalAmountApproved,
                totalAmountRejected
        );
    }

    private List<LoanRequest> filterLoanRequestByType(List<LoanRequest> requests, String type) {
        return requests.stream()
                .filter(request -> request.getType().toString().equals(type))
                .toList();
    }
    private List<LoanRequest> filterLoanRequestsByDateRange(List<LoanRequest> requests, LocalDate from, LocalDate to) {
        return requests.stream()
                .filter(request -> (request.getIssueDate().toLocalDate().isEqual(from) || request.getIssueDate().toLocalDate().isBefore(from)) &&
                        (request.getIssueDate().toLocalDate().isEqual(to) || request.getIssueDate().toLocalDate().isBefore(to)))
                .toList();
    }

    private List<LeaveRequest> filterLeaveRequestsByDepartment(List<LeaveRequest> requests, String department) {
        return requests.stream()
                .filter(request -> request.getEmployee().getEmployeeProfessionalDetails().getDepartment().toString().equals(department))
                .toList();
    }
    private List<LeaveRequest> filterLeaveRequestsByEntity(List<LeaveRequest> requests, String entity) {
        return requests.stream()
                .filter(request -> request.getEmployee().getEmployeeProfessionalDetails().getEntity().toString().equals(entity))
                .toList();
    }
}
