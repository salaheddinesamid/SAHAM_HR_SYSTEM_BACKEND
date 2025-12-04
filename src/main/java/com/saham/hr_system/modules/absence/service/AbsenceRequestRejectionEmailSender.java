package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;

public interface AbsenceRequestRejectionEmailSender {

    void notifyEmployee(AbsenceRequest absenceRequest);
}
