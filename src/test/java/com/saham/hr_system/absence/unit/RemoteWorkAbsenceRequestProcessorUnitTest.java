package com.saham.hr_system.absence.unit;

import com.saham.hr_system.modules.absence.repo.AbsenceRequestRepo;
import com.saham.hr_system.modules.absence.service.implementation.RemoteWorkAbsenceRequestProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RemoteWorkAbsenceRequestProcessorUnitTest {

    @Mock
    private AbsenceRequestRepo absenceRequestRepo;

    @InjectMocks
    private RemoteWorkAbsenceRequestProcessor remoteWorkAbsenceRequestProcessor;

    @Test
    void testRequestRemoteAbsenceSuccess(){}

    @Test
    void testRequestRemoteAbsenceFailure(){}
}
