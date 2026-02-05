package com.saham.hr_system.modules.loan.dto;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoanRequestResponseDto {
    private Long id;
    private String refNumber;
    private double amount;
    private String type;
    private String motif;
    private LocalDateTime issueDate;
    private LocalDate dateOfCollection;
    private boolean isApprovedByHr;
    private boolean isApprovedByFinanceDepartment;
    private String status;
    private EmployeeDetails employeeDetails;

    public LoanRequestResponseDto(
            LoanRequest loanRequest
    ){
        this.id = loanRequest.getRequestId();
        this.refNumber = loanRequest.getReferenceNumber();
        this.employeeDetails = new EmployeeDetails(loanRequest.getEmployee()); // extract employee details from the loan request
        this.amount = loanRequest.getAmount();
        this.type = loanRequest.getType().toString();
        this.motif = loanRequest.getMotif();
        this.dateOfCollection = loanRequest.getDateOfCollection();
        this.isApprovedByHr = loanRequest.isApprovedByHrDepartment();
        this.isApprovedByFinanceDepartment = loanRequest.isApprovedByFinanceDepartment();
        this.status = loanRequest.getStatus().toString();
        this.issueDate = loanRequest.getIssueDate();
    }
}

@Data
class EmployeeDetails{
    private String employeeName;
    private String employeeMatriculation;
    private String occupation;
    private String entity;

    public EmployeeDetails(Employee employee){
        this.employeeName = employee.getFullName();
        this.employeeMatriculation = employee.getMatriculation();
        this.occupation = employee.getOccupation();
        this.entity = employee.getEntity();
    }
}
