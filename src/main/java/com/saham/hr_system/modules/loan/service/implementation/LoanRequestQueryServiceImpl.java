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
    public List<LoanRequestResponseDto> getAllEmployeeRequests(String email) {
        // fetch the employee requests from db:
        Employee employee =
                employeeRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));
        // fetch the requests from db:
        List<LoanRequest> requests =
                loanRequestRepository.findAllByEmployee(employee);

        // map the loan requests:
        return
                requests.stream().map(LoanRequestResponseDto::new).toList();
    }

    @Override
    public List<LoanRequestResponseDto> getAllRequests() {
        // fetch the requests from db:
        List<LoanRequest> requests = loanRequestRepository.findAll();

        // map the loan requests:
        return
                requests.stream().map(LoanRequestResponseDto::new).toList();
    }
}
