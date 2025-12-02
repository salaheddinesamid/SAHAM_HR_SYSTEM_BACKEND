package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;
import java.util.List;

/**
 * AbsenceRequestQuery interface defines methods for querying absence requests.
 */
public interface AbsenceRequestQuery {

    /**
     *
     * @param email
     * @return
     */
    List<AbsenceRequestDetails> getAllMyAbsenceRequests(String email);

    /**
     *
     * @param managerEmail
     * @return
     */
    List<AbsenceRequestDetails> getAllSubordinateAbsenceRequests(String managerEmail);

    /**
     * This method returns the absence request that are approved, rejected, or waiting for HR review.
     * @return
     */
    List<AbsenceRequestDetails> getAllForHR();
}
