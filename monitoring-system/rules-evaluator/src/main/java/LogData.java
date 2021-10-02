import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class LogData implements Comparable<LogData> {
    private String componentName;
    private String type;
    private String message;
    private LocalDate date;
    private LocalTime time;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public long getEpochSeconds() {
        return LocalDateTime.of(this.date, this.time).toEpochSecond(ZoneOffset.UTC);
    }

    @Override
    public int compareTo(LogData other) {
        long mySeconds = this.getEpochSeconds();
        long otherSeconds = other.getEpochSeconds();
        return Long.compare(mySeconds, otherSeconds);
    }
}
