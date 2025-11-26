package com.saham.hr_system.unit;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestMapperImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestServiceImpl;
import com.saham.hr_system.modules.absence.service.implementation.AbsenceRequestValidatorImpl;
import com.saham.hr_system.modules.absence.service.implementation.RemoteWorkAbsenceRequestProcessor;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AbsenceServiceUnitTest {

    @Mock
    private AbsenceRequestRepo absenceRequestRepo;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AbsenceRequestMapperImpl absenceRequestMapper;

    @Mock
    private AbsenceRequestValidatorImpl absenceRequestValidator;

    @InjectMocks
    private RemoteWorkAbsenceRequestProcessor remoteWorkAbsenceRequestProcessor;

    @InjectMocks
    private AbsenceRequestServiceImpl absenceRequestService;

    private Employee employee;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setEmail("test@saham.com");
    }

    @Test
    void testProcessRemoteWorkAbsenceSuccess() throws Exception {

        // Arrange:
        AbsenceRequestDto requestDto = new AbsenceRequestDto();
        requestDto.setStartDate(LocalDate.of(2025,4,10));
        requestDto.setEndDate(LocalDate.of(2025,4,19));
        requestDto.setType("REMOTE_WORK");

        when(employeeRepository.findByEmail("test@saham.com")).thenReturn(Optional.of(employee));
        when(absenceRequestMapper.mapToEntity(requestDto)).thenReturn(new AbsenceRequest());
        // Act:
        remoteWorkAbsenceRequestProcessor.processAbsenceRequest("test@saham.com",requestDto);
        // verify:
        verify(absenceRequestRepo, times(1)).save(any());
    }

    @Test
    void testProcessSicknessAbsenceRequestSuccess() throws Exception {

        // Mock Multipart file:
        String fileName = "test-file.txt";
        String originalFileName = "original-test-file.txt";
        String contentType = "text/plain";
        byte[] content = "This is the content of the mock file.".getBytes();

        MockMultipartFile mockFile = new MockMultipartFile(
                fileName,
                originalFileName,
                contentType,
                content
        );
        // Arrange:
        AbsenceRequestDto requestDto = new AbsenceRequestDto();
        requestDto.setStartDate(LocalDate.of(2025,4,10));
        requestDto.setEndDate(LocalDate.of(2025,4,19));
        requestDto.setType("SICKNESS");
        requestDto.setMedicalCertificate(mockFile);

        when(employeeRepository.findByEmail("test@saham.com")).thenReturn(Optional.of(employee));
        when(absenceRequestMapper.mapToEntity(requestDto)).thenReturn(new AbsenceRequest());
        // Act:
        remoteWorkAbsenceRequestProcessor.processAbsenceRequest("test@saham.com",requestDto);
        // verify:
        verify(absenceRequestRepo, times(1)).save(any());
    }

    @Test
    void testRequestAbsenceSuccess() throws Exception {

        // Mock the entity:
        AbsenceRequest absenceRequest = new AbsenceRequest();
        absenceRequest.setAbsenceRequestId(1L);
        absenceRequest.setEmployee(employee);
        absenceRequest.setStartDate(LocalDate.of(2025,4,10));
        absenceRequest.setEndDate(LocalDate.of(2025,4,19));
        absenceRequest.setIssueDate(LocalDateTime.now());
        absenceRequest.setStatus(AbsenceRequestStatus.IN_PROCESS);
        absenceRequest.setApprovedByManager(false);
        absenceRequest.setApprovedByHr(false);

        // Mock the request:
        AbsenceRequestDto requestDto = new AbsenceRequestDto();
        requestDto.setStartDate(LocalDate.of(2025,4,10));
        requestDto.setEndDate(LocalDate.of(2025,4,19));
        requestDto.setType("REMOTE_WORK");

        // Arrange:
        when(employeeRepository.findByEmail("test@saham.com")).thenReturn(Optional.of(employee));

        // Act:
        AbsenceRequestDetails response = absenceRequestService.requestAbsence("test@saham.com",requestDto);
        ArgumentCaptor<AbsenceRequest> captor = ArgumentCaptor.forClass(AbsenceRequest.class);
        verify(absenceRequestRepo).save(captor.capture());
        AbsenceRequest savedRequest = captor.getValue();

        assertEquals(AbsenceType.REMOTE_WORK, savedRequest.getType());
        assertEquals(LocalDate.of(2025,4,10), savedRequest.getStartDate());
        assertEquals(employee, savedRequest.getEmployee());

    }

    @Test
    void testProcessRemoteWorkShouldThrowEmployeeNotFoundException(){}


}
