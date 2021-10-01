package ir.sahab.monitoringsystem.backend.domain.service;

import ir.sahab.monitoringsystem.backend.application.WarningDto;
import ir.sahab.monitoringsystem.backend.domain.model.WarningRepository;
import ir.sahab.monitoringsystem.backend.domain.model.Warning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarningService {


    private final WarningRepository warningRepository;

    @Autowired
    public WarningService(WarningRepository warningRepository) {
        this.warningRepository = warningRepository;
    }

    public List<WarningDto> getAllWarnings() {
        List<Warning> warnings = warningRepository.findAllOrderByDateAndTime();
        return warnings.stream().map(w ->
                new WarningDto(
                        w.getId(),
                        w.getRuleName(),
                        w.getComponentName(),
                        w.getType(),
                        w.getMessage(),
                        w.getDate(),
                        w.getTime()
                )
        ).collect(Collectors.toList());
    }
}
