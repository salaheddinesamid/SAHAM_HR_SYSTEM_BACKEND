package com.saham.hr_system.batches;

import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class HolidayUpdateEventListener {
    /**
     * This function listens to every holiday changes in the database, and perform a batch processing to update employee leaves affected.
     */
    //@EventListener(HolidayModifierImpl.class)
}
