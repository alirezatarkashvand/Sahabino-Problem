package ir.sahab.monitoringsystem.backend.api;

import ir.sahab.monitoringsystem.backend.application.WarningDto;
import ir.sahab.monitoringsystem.backend.domain.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WarningController {

    private final WarningService warningService;

    @Autowired
    public WarningController(WarningService warningService){
        this.warningService = warningService;
    }

    @RequestMapping(value = "/warning")
    public List<WarningDto> getAllWarnings() {
        return warningService.getAllWarnings();
    }
}


