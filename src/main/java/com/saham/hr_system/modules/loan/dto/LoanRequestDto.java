package com.saham.hr_system.modules.loan.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDto {

    private String loanType;
    private String motif;
    private double amount;
}
