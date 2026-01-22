package com.saham.hr_system.modules.payroll.service;

import com.saham.hr_system.modules.payroll.dto.EmployeePayrollDetailsDto;
import com.saham.hr_system.modules.payroll.dto.PayrollHistoryDto;

import java.io.IOException;
import java.util.List;

public interface PayrollQueryService {
    /**
     * Get all payroll history records.
     * @return list of all payroll history records.
     */
    List<PayrollHistoryDto> getAllPayrollsHistory();

    /**
     * Get yearly payrolls for a specific employee by email.
     * @param email : Employee's email
     * @param year : The corresponding year
     * @return list of employee payrolls from thz file system.
     */
    EmployeePayrollDetailsDto getYearlyPayrolls(String email, int year) throws IOException;
}
