package com.saham.hr_system.service.implementation;

import com.saham.hr_system.dto.LoanRequestDto;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.model.Employee;
import com.saham.hr_system.model.LoanRequest;
import com.saham.hr_system.repository.EmployeeRepository;
import com.saham.hr_system.repository.LoanRequestRepository;
import com.saham.hr_system.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final EmployeeRepository employeeRepository;
    private final LoanRequestRepository loanRequestRepository;

    @Autowired
    public LoanServiceImpl(EmployeeRepository employeeRepository, LoanRequestRepository loanRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.loanRequestRepository = loanRequestRepository;
    }

    @Override
    public void requestLoan(String email, LoanRequestDto requestDto) {
        // Fetch the employee:
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // Create new loan request:
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setEmployee(employee);
        loanRequest.setAmount(requestDto.getAmount());
        loanRequest.setMotif(requestDto.getMotif());
        loanRequest.setCollectionDate(requestDto.getCollectionDate());
        loanRequest.setPaymentModularity(requestDto.getPaymentModularity());
        loanRequest.setApprovedByFinanceDepartment(false);
        loanRequest.setApprovedByFinanceDepartment(false);

        // save the request:
        loanRequestRepository.save(loanRequest);
    }
}
