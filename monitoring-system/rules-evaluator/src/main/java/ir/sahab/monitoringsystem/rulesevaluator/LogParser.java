package ir.sahab.monitoringsystem.rulesevaluator;

import ir.sahab.monitoringsystem.rulesevaluator.common.LogData;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    public static List<LogData> parse(List<Pair<String, String>> logFileList) {
        List<LogData> logDataList = new ArrayList<>();

        for(Pair<String, String> logFile : logFileList) {
            String fileName = logFile.getValue0();
            String componentName = componentName(fileName);
            String fileContent = logFile.getValue1();
            String[] logs = splitLines(fileContent);
            for (String log : logs) {
                LogData logData = parseSingleLog(log);
                if (logData != null) {
                    logData.setComponentName(componentName);
                    logDataList.add(logData);
                }
            }
        }

        return logDataList;
    }

    private static String componentName(String fileName) {
        int COMPONENT_NAME_LOCATION = 0;
        return fileName.split("[-]")[COMPONENT_NAME_LOCATION];
    }

    private static String[] splitLines(String fileContent) {
        return fileContent.split("[$][$]");
    }


    private static LogData parseSingleLog(String log) {
        Pattern pattern = Pattern.compile("([\\d]{4})-([\\d]{2})-([\\d]{2})\\s([\\d]{2}):([\\d]{2}):?([\\d]{2})?,\\d+\\s\\[\\w+]\\s([A-z]+)\\s[\\w.]+\\s-\\s(.*)");
        Matcher matcher = pattern.matcher(log);

        LogData logData = new LogData();
        if(matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = Integer.parseInt(matcher.group(5));
            int second = Integer.parseInt(matcher.group(6) != null ? matcher.group(6) : "0");
            String type = matcher.group(7);
            String message = matcher.group(8);

            logData.setDate(LocalDate.of(year, month, day));
            logData.setTime(LocalTime.of(hour, minute, second));
            logData.setType(type);
            logData.setMessage(message);
            return logData;
        } else {
            System.out.println("LOG BAD FORMAT.");
            return null;
        }
    }

}
