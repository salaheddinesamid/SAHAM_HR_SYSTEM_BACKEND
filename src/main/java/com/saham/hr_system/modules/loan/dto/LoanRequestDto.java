package com.saham.hr_system.modules.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {

    private String loanType;
    private double amount;
    private String motif;
    private LocalDate dateOfCollection;

}
