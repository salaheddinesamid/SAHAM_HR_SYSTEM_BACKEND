package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;
import com.saham.hr_system.modules.absence.service.AbsenceRequestEmailSender;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class AbsenceRequestEmailSenderImpl implements AbsenceRequestEmailSender {
    @Override
    public void notifyEmployee(AbsenceRequest absenceRequest) throws MessagingException {

    }

    @Override
    public void notifyManager(AbsenceRequest absenceRequest) throws MessagingException {

    }
}
