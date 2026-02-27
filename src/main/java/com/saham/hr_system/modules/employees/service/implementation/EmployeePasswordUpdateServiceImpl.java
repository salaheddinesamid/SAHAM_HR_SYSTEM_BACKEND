package com.saham.hr_system.modules.employees.service.implementation;

import com.saham.hr_system.exception.UserNotFoundException;
import com.saham.hr_system.modules.employees.dto.PasswordUpdateDto;
import com.saham.hr_system.modules.employees.exception.InvalidOldPasswordException;
import com.saham.hr_system.modules.employees.model.Employee;
import com.saham.hr_system.modules.employees.repository.EmployeeRepository;
import com.saham.hr_system.modules.employees.service.EmployeePasswordUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeePasswordUpdateServiceImpl implements EmployeePasswordUpdateService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeePasswordUpdateServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean verifyOldPassword(String oldPassword, String password) {
        // Check if the old password matches the current password of the employee
        return passwordEncoder.matches(oldPassword, password);
    }

    @Override
    public void updatePassword(String email, PasswordUpdateDto passwordUpdateDto) {
        // Fetch the employee from the database
        Employee employee = employeeRepository
                .findByEmail(email).orElseThrow(()-> new UserNotFoundException(email));

        // Verify old password:
        if (!verifyOldPassword(passwordUpdateDto.getOldPassword(), employee.getPassword())) {
            log.info("Employee Password : {}", employee.getPassword());
            log.info("Provided Old Password : {}", passwordUpdateDto.getOldPassword());
            throw new InvalidOldPasswordException("Old password is incorrect");
        }
        // Otherwise:
        employee.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
        employeeRepository.save(employee);
    }
}
