package com.saham.hr_system.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestDto {

    private String loanType;
    private String motif;
    private double amount;
    private String paymentModularity;
    private LocalDate collectionDate;

}
