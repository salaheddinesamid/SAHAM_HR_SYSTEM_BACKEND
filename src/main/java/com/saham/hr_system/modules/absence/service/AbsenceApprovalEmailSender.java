package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.Absence;
import jakarta.mail.MessagingException;

/**
 * Interface for sending email notifications related to approved absences.
 * <p>
 * Implementations should handle notifying the employee and their manager
 * when an absence is approved.
 */
public interface AbsenceApprovalEmailSender {

    /**
     * Sends an email notification to the employee about the approved absence.
     *
     * @param absence the approved absence
     * @throws MessagingException if sending the email fails
     */
    void notifyEmployee(Absence absence) throws MessagingException;

    /**
     * Sends an email notification to the manager about the approved absence.
     *
     * @param absence the approved absence
     * @throws MessagingException if sending the email fails
     */
    void notifyManager(Absence absence) throws MessagingException;
}
