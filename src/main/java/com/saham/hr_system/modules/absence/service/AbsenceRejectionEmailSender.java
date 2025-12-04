package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;

public interface AbsenceRejectionEmailSender {

    void notifyEmployee(AbsenceRequest absenceRequest);
    void notifyManager(AbsenceRequest absenceRequest);
}
