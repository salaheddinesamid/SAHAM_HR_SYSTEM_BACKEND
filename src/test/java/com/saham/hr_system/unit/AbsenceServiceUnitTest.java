package com.saham.hr_system.unit;

import com.saham.hr_system.exception.UnauthorizedAccessException;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;
import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.model.AbsenceRequestStatus;
import com.saham.hr_system.modules.absence.model.AbsenceType;
import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.implementation.*;
import com.saham.hr_system.modules.absence.utils.AbsenceReferenceNumberGenerator;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.utils.TotalDaysCalculator;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private TotalDaysCalculator totalDaysCalculator;

    @Mock
    private RemoteWorkAbsenceRequestProcessor remoteWorkAbsenceRequestProcessor;

    @InjectMocks
    private AbsenceRequestServiceImpl absenceRequestService;

    @InjectMocks
    private AbsenceReferenceNumberGenerator absenceReferenceNumberGenerator;

    @InjectMocks
    private AbsenceRejectionImpl absenceRejection;

    private Employee manager;
    private Employee falseManager;
    private Employee employee;
    private AbsenceRequest absenceRequest1;
    private AbsenceRequest absenceRequest2;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        manager = new Employee();
        manager.setId(3L);
        manager.setEmail("manager.test@saham.com");

        falseManager = new Employee();
        falseManager.setId(4L);
        falseManager.setEmail("manager@saham.com");

        employee = new Employee();
        employee.setId(1L);
        employee.setEmail("test@saham.com");
        employee.setManager(manager);

        absenceRequest1 = new AbsenceRequest();
        absenceRequest1.setAbsenceRequestId(1L);
        absenceRequest1.setReferenceNumber("REF123");
        absenceRequest1.setStatus(AbsenceRequestStatus.IN_PROCESS);
        absenceRequest1.setEmployee(employee);
        absenceRequest1.setApprovedByManager(false);
        absenceRequest1.setApprovedByHr(false);
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

    @Test
    void approveSubordinateRemoteWorkAbsence(){
    }

    @Test
    void approveRemoteWorkAbsence(){}

    @Test
    void rejectSubordinateRemoteWorkAbsence(){
        // Arrange:
        when(absenceRequestRepo.findByReferenceNumber("REF123")).thenReturn(Optional.of(absenceRequest1));
        when(employeeRepository.findByEmail("manager.test@saham.com")).thenReturn(Optional.of(manager));
        // Act:
        absenceRejection.rejectSubordinate("manager.test@saham.com","REF123");
        // Verify:
        verify(absenceRequestRepo, times(1)).save(any());
    }

    @Test
    void rejectSubordinateRemoteWorkUnauthorizedAccess(){
        // Arrange:
        when(absenceRequestRepo.findByReferenceNumber("REF123")).thenReturn(Optional.of(absenceRequest1));
        when(employeeRepository.findByEmail("manager@saham.com")).thenReturn(Optional.of(falseManager));
        // Act:
        assertThrows(UnauthorizedAccessException.class, ()-> absenceRejection.rejectSubordinate("manager@saham.com","REF123"));
    }

    @Test
    void rejectRemoteWorkAbsence(){}

    @Test
    void testGenerateAbsenceReferenceNumber(){
        String refNumber =
                absenceReferenceNumberGenerator
                        .generate(absenceRequest1);
        System.out.println("Generated Reference Number: " + refNumber);
    }

}
