package com.saham.hr_system.modules.payroll.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.payroll.dto.EmployeePayrollDetailsDto;
import com.saham.hr_system.modules.payroll.dto.PayrollDetailsDto;
import com.saham.hr_system.modules.payroll.dto.PayrollHistoryDto;
import com.saham.hr_system.modules.payroll.model.PayrollHistory;
import com.saham.hr_system.modules.payroll.repository.PayrollHistoryRepository;
import com.saham.hr_system.modules.payroll.service.PayrollQueryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class PayrollQueryServiceImpl implements PayrollQueryService {
    private final PayrollHistoryRepository payrollHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollFileServiceImpl payrollFileService;

    public PayrollQueryServiceImpl(PayrollHistoryRepository payrollHistoryRepository, EmployeeRepository employeeRepository, PayrollFileServiceImpl payrollFileService) {
        this.payrollHistoryRepository = payrollHistoryRepository;
        this.employeeRepository = employeeRepository;
        this.payrollFileService = payrollFileService;
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

    @Override
    public EmployeePayrollDetailsDto getYearlyPayrolls(String matriculation, int year) throws IOException {
        // fetch the employee from DB:
        /*
        Employee employee =
                employeeRepository.findByEmail(email)
                        .orElseThrow(()-> new UserNotFoundException("Employee with email " + email + " not found."));

         */
        List<PayrollDetailsDto> payrollsOverview =
                payrollFileService.getPayrollsOverview(
                        matriculation,
                        year
                );
        return new EmployeePayrollDetailsDto(
                year,
                payrollsOverview
        );
    }
}
