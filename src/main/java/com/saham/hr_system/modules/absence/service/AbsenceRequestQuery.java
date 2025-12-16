package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import org.springframework.data.domain.Page;

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
     * @param page  the page number for pagination
     * @param size  the number of records per page
     * @return a list of detailed absence request responses
     */
    Page<AbsenceRequestDetails> getAllMyAbsenceRequests(String email, int page, int size);

    /**
     * Retrieves all absence requests submitted by employees who report
     * to the specified manager.
     *
     * @param managerEmail the email address of the manager
     * @param page         the page number for pagination
     * @param size         the number of records per page
     * @return a list of absence request details for subordinate employees
     */
    Page<AbsenceRequestDetails> getAllSubordinateAbsenceRequests(String managerEmail, int page, int size);

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
     * @param page
     * @param size
     * @return a list of absence request details visible to HR
     */
    Page<AbsenceRequestDetails> getAllForHR(int page, int size);
}
