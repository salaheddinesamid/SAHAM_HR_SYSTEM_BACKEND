package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import jakarta.mail.MessagingException;

/**
 * Interface for sending email notifications related to absence request approvals.
 * <p>
 * Implementations should handle notifying the employee and HR when an absence
 * request is approved or processed.
 */
public interface AbsenceRequestApprovalEmailSender {

    /**
     * Notifies the employee that their absence request has been approved.
     *
     * @param absenceRequest the approved absence request
     * @throws MessagingException if sending the email fails
     */
    void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException;

    /**
     * Notifies the HR team that an absence request has been approved.
     *
     * @param absenceRequest the approved absence request
     * @throws MessagingException if sending the email fails
     */
    void notifyHR(AbsenceRequest absenceRequest) throws MessagingException;
}
