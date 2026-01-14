package com.saham.hr_system.batches;

import com.saham.hr_system.modules.holidays.service.implementation.HolidayModifierImpl;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class HolidayUpdateBatch {
    /**
     * This function listens to every holiday changes in the database, and perform a batch processing to update employee leaves affected.
     */
    @EventListener(HolidayModifierImpl.class)
}
