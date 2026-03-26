package com.saham.hr_system.modules.auth.service;

public interface EmployeeAccountReactivation {
    /**     * Reactivates an employee account based on the provided email.
     *
     * @param email The email of the employee whose account is to be reactivated.
     */
    void reactivateEmployeeAccount(String email);
}
