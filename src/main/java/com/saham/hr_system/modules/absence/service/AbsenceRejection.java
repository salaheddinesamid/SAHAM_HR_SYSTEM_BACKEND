package com.saham.hr_system.modules.absence.service;

public interface AbsenceRejection {
    /**
     * Reject a subordinate's absence request.
     * @param email
     * @param refNumber
     */
    void rejectSubordinate(String email, String refNumber);
    /**
     * Reject an absence request by HR.
     * @param refNumber
     */
    void rejectAbsence(String refNumber);
}
