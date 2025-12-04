package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.Absence;
import jakarta.mail.MessagingException;

public interface AbsenceApprovalEmailSender {
    /**
     *
     * @param absence
     * @throws MessagingException
     */
    void notifyEmployee(Absence absence) throws MessagingException;

    /**
     *
     * @param absence
     * @throws MessagingException
     */
    void notifyManager(Absence absence) throws MessagingException;
}
