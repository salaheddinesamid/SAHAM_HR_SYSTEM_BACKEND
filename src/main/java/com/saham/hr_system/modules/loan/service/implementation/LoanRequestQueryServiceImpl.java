package com.saham.hr_system.modules.loan.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.loan.model.LoanRequestStatus;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.LoanRequestQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class LoanRequestQueryServiceImpl implements LoanRequestQueryService {

    private final LoanRequestRepository loanRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LoanRequestQueryServiceImpl(LoanRequestRepository loanRequestRepository, EmployeeRepository employeeRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<LoanRequestResponseDto> getAllEmployeeRequests(String email, int page, int size) {
        // fetch the employee requests from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        Pageable pageable = PageRequest.of(page, size, Sort.by("issueDate").descending());
        // fetch the requests from db:
        Page<LoanRequest> requests =
                loanRequestRepository.findAllByEmployee(employee, pageable);

        // map the loan requests:
        return
                requests.map(LoanRequestResponseDto::new);
    }

    @Override
    public Page<LoanRequestResponseDto> getAllRequests(
            int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("issueDate").descending());
        // fetch the requests from db:
        Page<LoanRequest> requests = loanRequestRepository.findAll(
                pageable
        );
        // map the loan requests:
        return
                requests.map(LoanRequestResponseDto::new);
    }
}
