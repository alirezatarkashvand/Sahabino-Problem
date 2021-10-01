package ir.sahab.monitoringsystem.backend.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name="warning")
public class Warning {

    @Id
    @Column(name = "ID")
    Long id;

    @Column(name = "RuleName")
    String ruleName;

    @Column(name = "ComponentName")
    String componentName;

    @Column(name = "WarningType")
    String type;

    @Column(name = "Message")
    String message;

    @Column(name = "WarningDate")
    LocalDate date;

    @Column(name = "WarningTime")
    LocalTime time;

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

    public void setId(Long id) {
        this.id = id;
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
