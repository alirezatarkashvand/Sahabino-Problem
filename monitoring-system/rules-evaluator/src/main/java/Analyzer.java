import java.util.*;

public class Analyzer {

    private static LogData lastLog;

    public static void updateWith(LogData logData) {
        lastLog = logData;
    }

    public static List<Warning> analyze() {
        List<Warning> warningList = new ArrayList<>();

        if(checkFirstRule()) {
            Warning warning = new Warning();
            warning.setRuleName(ApplicationProperties.getProperty("analyzer.rule.first.name"));
            warning.setComponentName(lastLog.getComponentName());
            warning.setType(lastLog.getType());
            warning.setMessage(lastLog.getMessage());
            warning.setDate(lastLog.getDate());
            warning.setTime(lastLog.getTime());
            warningList.add(warning);
        }

        return warningList;
    }

    private static boolean checkFirstRule() {
        return lastLog.getType().equals(ApplicationProperties.getProperty("analyzer.rule.first.type"));
    }

    private static boolean checkSecondRule() {
        return false;
    }

    private static boolean checkThirdRule() {
        return false;
    }
}
