package com.saham.hr_system.loan.unit;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.loan.dto.LoanRequestDto;
import com.saham.hr_system.modules.loan.repository.LoanRequestRepository;
import com.saham.hr_system.modules.loan.service.implementation.LoanRequestEmailSenderImpl;
import com.saham.hr_system.modules.loan.service.implementation.LoanRequestValidatorImpl;
import com.saham.hr_system.modules.loan.service.implementation.NormalLoanRequestProcessor;
import com.saham.hr_system.modules.loan.utils.LoanReferenceNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NormalLoanProcessorUnitTest {

    @Mock
    private LoanRequestRepository loanRequestRepository;

    @Mock
    private LoanRequestValidatorImpl loanRequestValidator;

    @Mock
    private LoanReferenceNumberGenerator loanReferenceNumberGenerator;

    @Mock
    private LoanRequestEmailSenderImpl loanRequestEmailSender;

    @InjectMocks
    private NormalLoanRequestProcessor normalLoanRequestProcessor;

    private Employee employee;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(2L);
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
}
