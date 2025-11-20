package com.saham.hr_system.unit;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.loan.repository.LoanRepository;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.implementation.LoanApprovalImpl;
import com.saham.hr_system.modules.loan.service.implementation.LoanRequestValidatorImpl;
import com.saham.hr_system.modules.loan.service.implementation.LoanServiceImpl;
import com.saham.hr_system.modules.loan.service.implementation.NormalLoanRequestProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LoanRequestRepository loanRequestRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanRequestValidatorImpl loanRequestValidator;

    @InjectMocks
    private NormalLoanRequestProcessor normalLoanRequestProcessor;

    @InjectMocks
    private LoanApprovalImpl loanApprovalImpl;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Employee employee;
    private LoanRequest loanRequest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Salaheddine");
        employee.setLastName("Samid");
        employee.setEmail("salaheddine@saham.com");

        loanRequest = new LoanRequest();
        loanRequest.setRequestId(1L);
        loanRequest.setEmployee(employee);
        loanRequest.setApprovedByFinanceDepartment(false);
        loanRequest.setApprovedByHrDepartment(false);
    }

    @Test
    void processNormalLoanRequestSuccess() throws Exception {
        LoanRequestDto requestDto = new LoanRequestDto();
        requestDto.setLoanType("NORMAL");
        requestDto.setAmount(23000);
        requestDto.setMotif("Personal expenses");

        // Act:
        normalLoanRequestProcessor.process(employee,requestDto);
        verify(loanRequestRepository, times(1)).save(any());

    }


    @Test
    void testApproveLoanRequestSuccess(){
        // Arrange:
        when(loanRequestRepository.findById(1L)).thenReturn(Optional.of(loanRequest));
        when(loanRequestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act:
        loanApprovalImpl.approveLoanRequest(1L);
        verify(loanRequestRepository, times(1)).save(any());
    }

    @Test
    void testRequestLoanShouldThrowEmployeeNotFound(){

        // Arrange:
        LoanRequestDto requestDto = new LoanRequestDto();
        requestDto.setAmount(2000);
        //requestDto.setCollectionDate(LocalDate.of(2025,12,1));
        requestDto.setLoanType("");
        requestDto.setMotif("");

        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(employee));

        when(loanRequestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(UserNotFoundException.class, ()-> // Act:
                loanService.requestLoan("test@gmail.com",requestDto));
        verify(loanRequestRepository, times(0)).save(any());
    }


}
