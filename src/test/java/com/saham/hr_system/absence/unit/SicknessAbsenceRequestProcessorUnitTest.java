package com.saham.hr_system.absence.unit;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestValidatorImpl;
import com.saham.hr_system.modules.absence.service.implementation.SicknessAbsenceDocumentStorageService;
import com.saham.hr_system.modules.absence.service.implementation.SicknessAbsenceRequestProcessor;
import com.saham.hr_system.modules.absence.utils.AbsenceReferenceNumberGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SicknessAbsenceRequestProcessorUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AbsenceRequestRepo absenceRequestRepo;

    @Mock
    private TotalDaysCalculator totalDaysCalculator;

    @Mock
    private AbsenceRequestValidatorImpl absenceRequestValidator;

    @Mock
    private AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator;

    @Mock
    private SicknessAbsenceDocumentStorageService sicknessAbsenceDocumentStorageService;

    @InjectMocks
    private SicknessAbsenceRequestProcessor processor;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessSicknessAbsenceSuccessfully() throws Exception {
        // GIVEN
        String email = "employee@test.com";

        AbsenceRequestDto dto = new AbsenceRequestDto();
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,5));
        dto.setMedicalCertificate(new MockMultipartFile(
                "file", "certificate.pdf", "application/pdf", "data".getBytes()
        ));

        Employee employee = new Employee();
        employee.setEmail(email);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john.smith@saham.com");

        when(employeeRepository.findByEmail(email))
                .thenReturn(Optional.of(employee));

        when(totalDaysCalculator.calculateTotalDays(any(), any()))
                .thenReturn(3l);

        when(absenceReferenceNumberGenerator.generate(any()))
                .thenReturn("ABS-20250101-0001");

        when(sicknessAbsenceDocumentStorageService.upload(anyString(), any()))
                .thenReturn("/certificates/cert.pdf");

        when(absenceRequestRepo.save(any(AbsenceRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        AbsenceRequest result =
                processor.processAbsenceRequest(email, dto);

        // THEN
        assertNotNull(result);
        assertEquals(AbsenceType.SICKNESS, result.getType());
        assertEquals(AbsenceRequestStatus.IN_PROCESS, result.getStatus());
        assertEquals(3.0, result.getTotalDays());
        assertEquals("ABS-20250101-0001", result.getReferenceNumber());
        assertEquals("/certificates/cert.pdf", result.getMedicalCertificatePath());

        //verify(absenceRequestValidator).validate(dto);
        verify(employeeRepository).findByEmail(email);
        verify(absenceRequestRepo).save(any(AbsenceRequest.class));
    }

}
