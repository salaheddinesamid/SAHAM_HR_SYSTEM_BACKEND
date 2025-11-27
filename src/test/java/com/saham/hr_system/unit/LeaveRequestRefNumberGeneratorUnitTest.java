package com.saham.hr_system.unit;

import com.saham.hr_system.modules.leave.repository.LeaveRequestRepository;
import com.saham.hr_system.modules.leave.utils.LeaveRequestRefNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

public class LeaveRequestRefNumberGeneratorUnitTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @InjectMocks
    private LeaveRequestRefNumberGenerator leaveRequestRefNumberGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateRefNumber() {
        // Implement test logic here
        when(leaveRequestRepository.countByRequestDateBetween(
                LocalDate.now().atStartOfDay(), LocalDate.now().atStartOfDay().plusDays(1)
        )).thenReturn(5L);

        String refNumber = leaveRequestRefNumberGenerator.generate();
        String refNumber1 = leaveRequestRefNumberGenerator.generate();
        String refNumber2 = leaveRequestRefNumberGenerator.generate();

        System.out.println(refNumber);
        System.out.println(refNumber1);
        System.out.println(refNumber2);

    }
}
