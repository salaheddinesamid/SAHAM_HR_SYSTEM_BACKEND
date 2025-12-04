package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import jakarta.mail.MessagingException;

public interface AbsenceRequestApprovalEmailSender {
    /**
     *
     * @param absenceRequest
     * @throws MessagingException
     */
    void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException;

    /**
     *
     * @param absenceRequest
     * @throws MessagingException
     */
    void notifyHR(AbsenceRequest absenceRequest) throws MessagingException;
}
