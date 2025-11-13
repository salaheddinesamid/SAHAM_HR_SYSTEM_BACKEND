package com.saham.hr_system.unit;

import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.dto.LoanRequestResponseDto;
import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.model.LoanRequest;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.implementation.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanServiceUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LoanRequestRepository loanRequestRepository;

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
    void testRequestLoanSuccess(){

        // Arrange:
        LoanRequestDto requestDto = new LoanRequestDto();
        requestDto.setAmount(2000);
        //requestDto.setCollectionDate(LocalDate.of(2025,12,1));
        requestDto.setLoanType("");
        requestDto.setMotif("");

        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(employee));

        when(loanRequestRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act:
        loanService.requestLoan(employee.getEmail(),requestDto);
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

    @Test
    void testGetAllEmployeeLoanRequestsSuccess(){
        // Arrange:
        when(employeeRepository.findByEmail("salaheddine@saham.com")).thenReturn(Optional.of(employee));
        when(loanRequestRepository.findById(1L)).thenReturn(Optional.of(loanRequest));

        // Act:
        List<LoanRequestResponseDto> res = loanService
                .getAllEmployeeRequests(employee.getEmail());

        verify(loanRequestRepository, times(1)).findAllByEmployee(employee);

        //
    }


}
