package com.saham.hr_system.modules.employees.service;

public interface EmployeeBalanceAccrualService {

    void processMonthlyAccruals();
    void processYearlyAccruals();
}
