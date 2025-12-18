package com.saham.hr_system.administration.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class EmployeeManagementServiceUnitTest {

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAllEmployeesSuccess(){}

    @Test
    void testAddNewEmployeeSuccess(){}

    @Test
    void testAddNewEmployeeThrowEmployeeAlreadyExists(){}
}
