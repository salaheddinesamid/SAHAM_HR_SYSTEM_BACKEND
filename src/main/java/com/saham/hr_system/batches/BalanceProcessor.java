package com.saham.hr_system.batches;

public interface BalanceProcessor {
    /**
     *
     * @param balanceId
     */
    void process(Long balanceId);
}
