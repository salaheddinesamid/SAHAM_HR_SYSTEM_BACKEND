package com.saham.hr_system.modules.absence.service.implementation;

import com.saham.hr_system.modules.absence.model.Absence;
import com.saham.hr_system.modules.absence.service.AbsenceApprovalEmailSender;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class AbsenceApprovalEmailSenderImpl implements AbsenceApprovalEmailSender {

    @Override
    public void notifyEmployee(Absence absence) throws MessagingException {

    }

    @Override
    public void notifyManager(Absence absence) throws MessagingException {

    }
}
