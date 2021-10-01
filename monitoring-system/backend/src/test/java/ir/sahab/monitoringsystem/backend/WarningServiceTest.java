package ir.sahab.monitoringsystem.backend;

import ir.sahab.monitoringsystem.backend.application.WarningDto;
import ir.sahab.monitoringsystem.backend.domain.model.Warning;
import ir.sahab.monitoringsystem.backend.domain.model.WarningRepository;
import ir.sahab.monitoringsystem.backend.domain.service.WarningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarningServiceTest {

    @Mock
    WarningRepository warningRepository;

    @InjectMocks
    WarningService warningService;

    @Test
    public void getAllWarnings_ReturnsAllWarnings() {
        List<Warning> expectedWarningList = new ArrayList<>();
        Warning warning = new Warning();
        warning.setId(1L);
        expectedWarningList.add(warning);
        when(warningRepository.findAllOrderByDateAndTime()).thenReturn(expectedWarningList);
        List<WarningDto> actualWarningList = warningService.getAllWarnings();

        assertEquals(1, actualWarningList.size());
        assertEquals( 1L, actualWarningList.get(0).getId());
    }
}
