package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.auth.service.implementation.EmployeePasswordSetupService;
import com.saham.hr_system.modules.employees.dto.*;
import com.saham.hr_system.modules.employees.mapper.*;
import com.saham.hr_system.modules.employees.model.*;
import com.saham.hr_system.modules.employees.repository.*;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeAdderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeAdderUnitTest {

    @InjectMocks
    private EmployeeAdderServiceImpl employeeAdderService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper;

    @Mock
    private EmployeeProfessionalDetailsRepository employeeProfessionalDetailsRepository;

    @Mock
    private EmployeeSocialDetailMapper employeeSocialDetailMapper;

    @Mock
    private EmployeeSocialDetailsRepository employeeSocialDetailsRepository;

    @Mock
    private EmployeeContactDetailsMapper employeeContactDetailsMapper;

    @Mock
    private EmployeeContactDetailsRepository employeeContactDetailsRepository;

    @Mock
    private EmployeeBalanceRepository employeeBalanceRepository;

    @Mock
    private EmployeePasswordSetupService employeePasswordSetupService;

    private NewEmployeeDto newEmployeeDto;

    @BeforeEach
    void setUp() {

        NewEmployeeProfessionalDetailsDto professionalDetailsDto = new NewEmployeeProfessionalDetailsDto(
                "MAT123456",
                "Software Engineer",
                "IT",
                "SAHAM_HORIZON",
                2L,
                LocalDate.of(2025,11,22),
                "Casablanca",
                "00",
                "salaheddine@saham.com",
                "00",
                "00"
        );
        NewEmployeeSocialDetails socialDetailsDto = new NewEmployeeSocialDetails(
                "CNSS123456",
                "CIMR123456",
                "INS123456",
                ""
        );
        NewEmployeeContactDetails contactDetails = new NewEmployeeContactDetails(
                "Dad",
                "+212612345678"
        );
        EmployeeBalanceDto balanceDto = new EmployeeBalanceDto(
                2025,
                25,
                25,
                2,
                0
        );
        newEmployeeDto = new NewEmployeeDto(
                "Salaheddine",
                "Samid",
                "MALE",
                "T573GH",
                LocalDate.of(2003,12, 3),
                "SINGLE",
                0,
                "salaheddine@saham.com",
                professionalDetailsDto,
                socialDetailsDto,
                contactDetails,
                List.of("EMPLOYEE", "MANAGER"),
                balanceDto
        );
    }

    @Test
    void shouldCreateEmployeeSuccessfully() {
        // GIVEN
        String matricule = "MAT001";

        NewEmployeeProfessionalDetailsDto professionalDetailsDto = new NewEmployeeProfessionalDetailsDto();
        professionalDetailsDto.setMatriculation(matricule);
        newEmployeeDto.setProfessionalDetailsDto(professionalDetailsDto);

        when(employeeRepository.existsByEmployeeProfessionalDetails_Matriculation(matricule))
                .thenReturn(false);

        Employee employee = new Employee();
        Map<String, Object> map = new HashMap<>();
        map.put("mappedEmployee", employee);
        map.put("rawPassword", "123456");

        when(employeeMapper.mapToEmployee(newEmployeeDto)).thenReturn(map);

        EmployeeProfessionalDetails professionalDetails = new EmployeeProfessionalDetails();
        professionalDetails.setProfessionalEmail("test@saham.com");
        professionalDetails.setEntity(EmployeeEntity.SAHAM_HORIZON);
        professionalDetails.setDepartment(EmployeeDepartment.IT);

        when(employeeProfessionalDetailsMapper
                .mapToEmployeeProfessionalDetails(any(), eq(false)))
                .thenReturn(professionalDetails);

        when(employeeProfessionalDetailsRepository.save(any()))
                .thenReturn(professionalDetails);

        EmployeeSocialDetails socialDetails = new EmployeeSocialDetails();
        when(employeeSocialDetailMapper.mapToEmployeeSocialDetails(any()))
                .thenReturn(socialDetails);
        when(employeeSocialDetailsRepository.save(any()))
                .thenReturn(socialDetails);

        EmployeeContactDetails contactDetails = new EmployeeContactDetails();
        when(employeeContactDetailsMapper.mapToEmployeeContactDetails(any()))
                .thenReturn(contactDetails);
        when(employeeContactDetailsRepository.save(any()))
                .thenReturn(contactDetails);

        EmployeeBalance balance = new EmployeeBalance();
        when(employeeMapper.mapToEmployeeBalanceDto(any()))
                .thenReturn(balance);
        when(employeeBalanceRepository.save(any()))
                .thenReturn(balance);

        when(employeeRepository.save(any())).thenReturn(employee);

        // WHEN
        EmployeeDetailsDto result = employeeAdderService.add(newEmployeeDto);

        // THEN
        assertNotNull(result);

        verify(employeeRepository).existsByEmployeeProfessionalDetails_Matriculation(matricule);
        verify(employeeProfessionalDetailsRepository).save(any());
        verify(employeeSocialDetailsRepository).save(any());
        verify(employeeContactDetailsRepository).save(any());
        verify(employeeBalanceRepository).save(any());
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowExceptionWhenMatriculeAlreadyExists() {

        // GIVEN
        String matricule = "MAT001";

        NewEmployeeProfessionalDetailsDto professionalDetailsDto = new NewEmployeeProfessionalDetailsDto();
        professionalDetailsDto.setMatriculation(matricule);
        newEmployeeDto.setProfessionalDetailsDto(professionalDetailsDto);

        when(employeeRepository.existsByEmployeeProfessionalDetails_Matriculation(matricule))
                .thenReturn(true);

        // WHEN + THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> employeeAdderService.add(newEmployeeDto)
        );

        assertTrue(exception.getMessage().contains("already exists"));

        verify(employeeRepository, never()).save(any());
    }
}
