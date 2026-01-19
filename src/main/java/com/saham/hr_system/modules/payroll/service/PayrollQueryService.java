package com.saham.hr_system.modules.payroll.service;

import com.saham.hr_system.modules.payroll.dto.PayrollHistoryDto;

import java.util.List;

public interface PayrollQueryService {
    /**
     * Get all payroll history records.
     * @return list of all payroll history records.
     */
    List<PayrollHistoryDto> getAllPayrollsHistory();
}
