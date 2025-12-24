package com.saham.hr_system.modules.employees.service;


import com.saham.hr_system.modules.employees.dto.EmployeeDetailsDto;
import com.saham.hr_system.modules.employees.dto.NewEmployeeDto;

public interface EmployeeAdderService {

    boolean verify();
    EmployeeDetailsDto add(NewEmployeeDto newEmployeeRequestDto);
}
