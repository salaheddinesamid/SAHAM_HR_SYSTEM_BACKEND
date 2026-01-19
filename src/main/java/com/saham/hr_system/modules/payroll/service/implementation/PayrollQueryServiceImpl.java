package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.modules.payroll.dto.PayrollHistoryDto;
import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import com.saham.hr_system.modules.payroll.repository.PayrollHistoryRepository;
import com.saham.hr_system.modules.payroll.service.PayrollQueryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PayrollQueryServiceImpl implements PayrollQueryService {
    private final PayrollHistoryRepository payrollHistoryRepository;

    public PayrollQueryServiceImpl(PayrollHistoryRepository payrollHistoryRepository) {
        this.payrollHistoryRepository = payrollHistoryRepository;
    }

    @Override
    public List<PayrollHistoryDto> getAllPayrollsHistory() {
        // Fetch all the payrolls from the database:
        List<PayrollHistory> payrollHistories = payrollHistoryRepository.findAll();
        return payrollHistories
                .stream()
                .map(PayrollHistoryDto::new)
                .toList();
    }
}
