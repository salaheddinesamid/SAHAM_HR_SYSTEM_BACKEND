package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.dto.AbsenceRequestDetails;

import java.util.List;

/**
 * AbsenceRequestQuery interface defines methods for querying absence requests.
 */
public interface AbsenceRequestQuery {

    List<AbsenceRequestDetails> getAllMyAbsenceRequests(String email);
    List<AbsenceRequestDetails> getAllSubordinateAbsenceRequests(String managerEmail);
}
