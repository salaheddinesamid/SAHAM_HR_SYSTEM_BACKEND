package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import java.util.List;

/**
 * Service interface defining query operations for retrieving absence requests.
 * <p>
 * This interface provides methods to fetch absence records for employees,
 * managers, and HR staff based on their respective roles and responsibilities.
 * </p>
 */
public interface AbsenceRequestQuery {

    /**
     * Retrieves all absence requests submitted by a specific employee.
     *
     * @param email the email address of the employee
     * @return a list of detailed absence request responses
     */
    List<AbsenceRequestDetails> getAllMyAbsenceRequests(String email);

    /**
     * Retrieves all absence requests submitted by employees who report
     * to the specified manager.
     *
     * @param managerEmail the email address of the manager
     * @return a list of absence request details for subordinate employees
     */
    List<AbsenceRequestDetails> getAllSubordinateAbsenceRequests(String managerEmail);

    /**
     * Retrieves all absence requests across the organization that require
     * or have undergone HR review.
     * <p>
     * This includes requests that are:
     * <ul>
     *     <li>Waiting for HR approval</li>
     *     <li>Approved by HR</li>
     *     <li>Rejected by HR</li>
     * </ul>
     * </p>
     *
     * @return a list of absence request details visible to HR
     */
    List<AbsenceRequestDetails> getAllForHR();
}
