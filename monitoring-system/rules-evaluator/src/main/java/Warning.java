import java.time.LocalDate;
import java.time.LocalTime;

public class Warning {
    private final String ruleName;
    private final String componentName;
    private final String type;
    private final String message;
    private final String date;
    private final String time;

    public Warning(String ruleName, String componentName, String type, String message, String date, String time) {
        this.ruleName = ruleName;
        this.componentName = componentName;
        this.type = type;
        this.message = message;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
