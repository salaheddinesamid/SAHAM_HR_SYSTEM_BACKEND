package com.saham.hr_system.modules.absence.service;

import com.saham.hr_system.modules.absence.model.AbsenceRequest;

/**
 * Contract defining approval operations for different absence request types.
 *
 * <p>Each implementation of this interface should handle a specific type of
 * absence (e.g., sickness, remote work, justified absence). The
 * {@link #supports(String)} method allows the system to determine which
 * implementation should process which type.</p>
 */
public interface AbsenceApproval {

    /**
     * Checks whether this implementation supports the specified absence type.
     *
     * @param type the type of absence request (string representation of {@link AbsenceType})
     * @return {@code true} if this implementation handles the given type, otherwise {@code false}
     */
    boolean supports(String type);

    /**
     * Approves an absence request submitted by a subordinate employee.
     *
     * <p>This method should verify that the user performing the approval
     * (identified by their email) is indeed the manager of the employee who
     * submitted the absence request. If not, an exception must be thrown.</p>
     *
     * @param approvedBy the email of the manager who is approving the request
     * @param absenceRequest the actual absence request
     *
     * @throws java.util.NoSuchElementException if no request or manager is found
     * @throws IllegalStateException if the user is not authorized to approve the request
     */
    void approveSubordinate(String approvedBy, AbsenceRequest absenceRequest);

    /**
     * Approves an absence request at the HR/global level.
     *
     * <p>This method is typically called after manager approval and may
     * perform final validation, update the status of the request, trigger
     * notifications, or perform additional workflow logic.</p>
     *
     * @param absenceRequest the actual absence request
     *
     * @throws java.util.NoSuchElementException if no matching request exists
     */
    void approve(AbsenceRequest absenceRequest);
}
