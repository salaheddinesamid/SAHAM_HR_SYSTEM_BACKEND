package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestApprovalEmailSender;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class AbsenceRequestApprovalImpl implements AbsenceRequestApprovalEmailSender {
    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException {

    }

    @Override
    public void notifyHR(AbsenceRequest absenceRequest) throws MessagingException {

    }
}
