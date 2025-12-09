package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import com.saham.hr_system.modules.absence.dto.AbsenceRequestDto;

/**
 * Service interface for handling absence requests.
 * <p>
 * Defines the contract for creating new absence requests, approving requests by managers,
 * and final approval by HR.
 */
public interface AbsenceRequestService {

    /**
     * Submits a new absence request for the given employee.
     *
     * @param email      the email of the employee submitting the request
     * @param requestDto the absence request details
     * @return the created {@link AbsenceRequestDetails} with full information
     * @throws Exception if the request cannot be processed
     */
    AbsenceRequestDetails requestAbsence(String email, AbsenceRequestDto requestDto) throws Exception;

    /**
     * Approves a subordinate's absence request by the manager.
     *
     * @param approvedBy the email of the manager approving the request
     * @param refNumber  the reference number of the absence request
     * @throws Exception if the request cannot be found or approved
     */
    void approveAbsenceRequest(String approvedBy, String refNumber) throws Exception;

    /**
     * Performs final approval of an absence request by HR.
     *
     * @param refNumber the reference number of the absence request
     * @throws Exception if the request cannot be found or approved
     */
    void approveAbsence(String refNumber) throws Exception;
}
