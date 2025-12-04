package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import jakarta.mail.MessagingException;

public interface AbsenceRequestEmailSender {
    /**
     * @param absenceRequest
     * @throws MessagingException
     */
    void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException;

    /**
     * @param absenceRequest
     * @throws MessagingException
     */
    void notifyManager(AbsenceRequest absenceRequest) throws MessagingException;
}
