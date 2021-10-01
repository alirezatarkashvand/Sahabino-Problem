package ir.sahab.monitoringsystem.backend.application;

import java.time.LocalDate;
import java.time.LocalTime;

public class WarningDto {

    Long id;
    String ruleName;
    String componentName;
    String type;
    String message;
    LocalDate date;
    LocalTime time;

    public WarningDto(Long id, String ruleName, String componentName, String type, String message, LocalDate date, LocalTime time) {
        this.id = id;
        this.ruleName = ruleName;
        this.componentName = componentName;
        this.type = type;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
