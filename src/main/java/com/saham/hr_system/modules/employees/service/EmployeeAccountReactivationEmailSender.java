package com.saham.hr_system.modules.employees.service;

public interface EmployeeAccountReactivationEmailSender {

    void sendReactivationEmail(String employeeEmail, String link);
}
