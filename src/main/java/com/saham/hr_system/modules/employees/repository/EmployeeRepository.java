package com.saham.hr_system.modules.employees.repository;

import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.model.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    /**
     *
     * @param email
     * @return
     */
    Optional<Employee> findByEmail(String email);

    /**
     *
     * @param id
     * @return
     */
    List<Employee> findAllByManagerId(Long id);

    /**
     *
     * @param roles
     * @return
     */
    List<Employee> findAllByRoles(List<Role> roles);

    /**
     *
     * @param email
     * @return
     */
    boolean existsByEmail(String email);

    boolean existsByEmployeeProfessionalDetails_Matriculation(String employeeProfessionalDetailsMatriculation);

    Optional<Employee> findByRolesAndFirstNameAndLastName(List<Role> roles, String firstName, String lastName);

    /**
     *
     * @param pageable
     * @return
     */
    @NotNull Page<Employee> findAll(@NotNull Pageable pageable);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Optional<Employee> findByEmployeeProfessionalDetails_Matriculation(String employeeProfessionalDetailsMatriculation);

}
