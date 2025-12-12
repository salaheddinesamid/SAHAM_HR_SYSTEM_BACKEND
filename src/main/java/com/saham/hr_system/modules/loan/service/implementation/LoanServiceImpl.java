package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.loan.service.LoanRequestProcessor;
import com.saham.hr_system.modules.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final EmployeeRepository employeeRepository;
    private final List<LoanRequestProcessor> processors;

    @Autowired
    public LoanServiceImpl(EmployeeRepository employeeRepository, List<LoanRequestProcessor> processors) {
        this.employeeRepository = employeeRepository;
        this.processors = processors;
    }

    @Override
    public void requestLoan(String email, LoanRequestDto requestDto) throws Exception {
        // Fetch the employee:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));
        // find the processor:
        LoanRequestProcessor processor =
                processors.stream().filter(p -> p.supports(requestDto.getLoanType())).findFirst().orElse(null);

        processor.process(employee, requestDto);
    }
}
