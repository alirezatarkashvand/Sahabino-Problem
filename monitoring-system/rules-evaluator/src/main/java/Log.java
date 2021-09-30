import java.time.LocalDate;
import java.time.LocalTime;

public class Log {
    private final String componentName;
    private final String type;
    private final String message;
    private final LocalDate date;
    private final LocalTime time;

    public Log(String componentName, String type, String message, LocalDate date, LocalTime time) {
        this.componentName = componentName;
        this.type = type;
        this.message = message;
        this.date = date;
        this.time = time;
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
