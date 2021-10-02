package ir.sahab.monitoringsystem.rulesevaluator.common;

import java.time.LocalDate;
import java.time.LocalTime;

public class Warning {
    private String ruleName;
    private String componentName;
    private String type;
    private String message;
    private LocalDate date;
    private LocalTime time;


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

    public String getDate() {
        return date.toString();
    }

    public String getTime() {
        return time.toString().split("[\\.]")[0];
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
