package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;

/**
 * Interface for sending email notifications related to rejected absence requests.
 * <p>
 * Implementations should notify both the employee and their manager when an
 * absence request is rejected.
 */
public interface AbsenceRejectionEmailSender {

    /**
     * Notifies the employee that their absence request has been rejected.
     *
     * @param absenceRequest the rejected absence request
     */
    void notifyEmployee(AbsenceRequest absenceRequest);

    /**
     * Notifies the manager that an absence request under their supervision has been rejected.
     *
     * @param absenceRequest the rejected absence request
     */
    void notifyManager(AbsenceRequest absenceRequest);
}
