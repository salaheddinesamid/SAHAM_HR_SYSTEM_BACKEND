package com.saham.hr_system.modules.employees.service;

public interface EmployeeBalanceAccrualService {
    /**
     * Process monthly accruals for all employees. This method should be scheduled to run at the end of each month.
     * This accrual runs every month except for January.
     */
    void processMonthlyAccruals();
    /**
     * Process yearly accruals for all employees. This method should be scheduled to run at the end of each year.
     * This accrual runs only once a year, at the end of December, to calculate the annual right and update the accumulated balance for each employee.
     */
    void processYearlyAccruals();
}
