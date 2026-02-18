package com.saham.hr_system.employee.unit;

import com.saham.hr_system.modules.employees.dto.NewEmployeeProfessionalDetailsDto;
import com.saham.hr_system.modules.employees.mapper.EmployeeProfessionalDetailsMapper;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.EmployeeProfessionalDetails;
import com.saham.hr_system.modules.employees.model.Role;
import com.saham.hr_system.modules.employees.model.RoleName;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.repository.RoleRepository;
import com.saham.hr_system.modules.employees.service.implementation.EmployeeQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EmployeeProfessionalDetailsMapperUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeQueryServiceImpl employeeQueryService;

    @InjectMocks
    private EmployeeProfessionalDetailsMapper employeeProfessionalDetailsMapper;

    private final Role managerRole = new Role();
    private Employee manager;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        // Initialize Manager
        manager = new Employee();
        manager.setId(1L);
        manager.setFirstName("Ciryane");
        manager.setLastName("EL KHIATI");
        managerRole.setRoleName(RoleName.MANAGER.toString());
    }

    @Test
    void testMapDtoToEmployeeProfessionalDetailsSuccess(){
        // Professional details dto
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
        // Arrange:
        when(roleRepository.findByRoleName("MANAGER")).thenReturn(Optional.of(managerRole));
        when(employeeRepository.findByRolesAndId(
                List.of(managerRole),
                2L
        )).thenReturn(Optional.of(manager));
        when(employeeQueryService.getManager(2L)).thenReturn(manager);

        // Act and Verify:
        EmployeeProfessionalDetails expected = employeeProfessionalDetailsMapper.mapToEmployeeProfessionalDetails(
                professionalDetailsDto,
                false
        );
        // Verify that the mapping is correct & all no null fields
        assert expected.getMatriculation().equals(professionalDetailsDto.getMatriculation());
        assertEquals("MAT123456", expected.getMatriculation());
        assertEquals("Software Engineer", expected.getOccupation());
        assertEquals("IT", expected.getDepartment().toString());
        assertEquals("SAHAM_HORIZON", expected.getEntity().toString());
        assertEquals(manager, expected.getManager());
        assertEquals( LocalDate.of(2025,11,22), expected.getJoinDate());
        assertEquals("Casablanca", expected.getSite());
        assertEquals("00", expected.getProfessionalPhoneNumber());
        assertEquals("salaheddine@saham.com", expected.getProfessionalEmail());
        assertEquals("00", expected.getProfessionalFixedPhoneNumber());
        assertEquals("00", expected.getExtension());
    }
}
