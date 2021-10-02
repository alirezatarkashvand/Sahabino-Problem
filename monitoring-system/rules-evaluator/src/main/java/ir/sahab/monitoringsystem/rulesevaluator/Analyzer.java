package ir.sahab.monitoringsystem.rulesevaluator;

import ir.sahab.monitoringsystem.rulesevaluator.common.OrderedList;
import ir.sahab.monitoringsystem.rulesevaluator.common.Status;
import ir.sahab.monitoringsystem.rulesevaluator.config.ApplicationProperties;
import ir.sahab.monitoringsystem.rulesevaluator.common.LogData;
import ir.sahab.monitoringsystem.rulesevaluator.common.Warning;

import java.util.*;

public class Analyzer {

    private static final String FIRST_RULE_NAME = ApplicationProperties.getProperty("analyzer.rules.first.name");
    private static final String SECOND_RULE_NAME = ApplicationProperties.getProperty("analyzer.rules.second.name");
    private static final String THIRD_RULE_NAME = ApplicationProperties.getProperty("analyzer.rules.third.name");

    private static final String FIRST_RULE_TYPE = ApplicationProperties.getProperty("analyzer.rules.first.type");
    private static final String SECOND_RULE_TYPE = ApplicationProperties.getProperty("analyzer.rules.second.type");

    private static final int SECOND_RULE_DURATION = Integer.parseInt(ApplicationProperties.getProperty("analyzer.rules.second.duration.minutes")) * 60;
    private static final int THIRD_RULE_DURATION = Integer.parseInt(ApplicationProperties.getProperty("analyzer.rules.third.duration.minutes")) * 60;

    private static final int SECOND_RULE_RATE = Integer.parseInt(ApplicationProperties.getProperty("analyzer.rules.second.rate"));
    private static final int THIRD_RULE_RATE = Integer.parseInt(ApplicationProperties.getProperty("analyzer.rules.third.rate"));

    private static final Map<String, OrderedList<LogData>> logRecords = new HashMap<>();
    private static LogData lastLog;

    public static void updateWith(LogData logData) {
        lastLog = logData;

        OrderedList<LogData> logList;
        if(logRecords.containsKey(lastLog.getComponentName())) {
            logList = logRecords.get(lastLog.getComponentName());
            logList.orderedAdd(lastLog);
        } else {
            logList = new OrderedList<>();
            logList.add(lastLog);
        }
        logRecords.put(lastLog.getComponentName(), logList);
    }

    private static void removeAccountedLogs(Status secondRuleStatus, Status thirdRuleStatus) {
        OrderedList<LogData> logList = logRecords.get(lastLog.getComponentName());
        int secondRuleBegin = secondRuleStatus.getBegin();
        int secondRuleEnd = secondRuleStatus.getEnd();
        int thirdRuleBegin = thirdRuleStatus.getBegin();
        int thirdRuleEnd = thirdRuleStatus.getEnd();

        if(secondRuleStatus.isViolated() && thirdRuleStatus.isViolated()) {
            if(secondRuleBegin > thirdRuleEnd){
                for(int i = secondRuleStatus.getEnd(); i >= secondRuleStatus.getBegin(); i--)
                    if(logList.get(i).getType().equals(SECOND_RULE_TYPE))
                        logList.remove(i);


                logList.subList(thirdRuleStatus.getBegin(), thirdRuleStatus.getEnd() + 1).clear();

            } else if(thirdRuleBegin > secondRuleEnd) {

                logList.subList(thirdRuleStatus.getBegin(), thirdRuleStatus.getEnd() + 1).clear();

                for(int i = secondRuleStatus.getEnd(); i >= secondRuleStatus.getBegin(); i--)
                    if(logList.get(i).getType().equals(SECOND_RULE_TYPE))
                        logList.remove(i);

            }

        } else if(secondRuleStatus.isViolated()) {

            for(int i = secondRuleStatus.getEnd(); i >= secondRuleStatus.getBegin(); i--)
                if(logList.get(i).getType().equals(SECOND_RULE_TYPE))
                    logList.remove(i);


        } else if(thirdRuleStatus.isViolated())
            logList.subList(thirdRuleStatus.getBegin(), thirdRuleStatus.getEnd() + 1).clear();

        logRecords.put(lastLog.getComponentName(), logList);
    }

    public static List<Warning> analyze() {
        List<Warning> warningList = new ArrayList<>();

        Status firstRuleStatus = firstRule();
        Status secondRuleStatus = secondRule();
        Status thirdRuleStatus = thirdRule();

        if(firstRuleStatus.isViolated()) {
            Warning warning = new Warning();
            warning.setRuleName(FIRST_RULE_NAME);
            warning.setComponentName(lastLog.getComponentName());
            warning.setType(lastLog.getType());
            warning.setMessage(lastLog.getMessage());
            warning.setDate(lastLog.getDate());
            warning.setTime(lastLog.getTime());
            warningList.add(warning);
        }

        if(secondRuleStatus.isViolated()) {
            Warning warning = new Warning();
            warning.setRuleName(SECOND_RULE_NAME);
            warning.setComponentName(lastLog.getComponentName());
            warning.setType(SECOND_RULE_TYPE);
            warning.setMessage("Rate: " + secondRuleStatus.getRate() + " Last Log Message: " + lastLog.getMessage());
            warning.setDate(lastLog.getDate());
            warning.setTime(lastLog.getTime());
            warningList.add(warning);
        }

        if(thirdRuleStatus.isViolated()) {
            Warning warning = new Warning();
            warning.setRuleName(THIRD_RULE_NAME);
            warning.setComponentName(lastLog.getComponentName());
            warning.setType(lastLog.getType());
            warning.setMessage("Rate: " + thirdRuleStatus.getRate());
            warning.setDate(lastLog.getDate());
            warning.setTime(lastLog.getTime());
            warningList.add(warning);
        }

        removeAccountedLogs(secondRuleStatus, thirdRuleStatus);

        return warningList;
    }

    private static Status firstRule() {
        Status status = new Status();
        if(lastLog.getType().equals(FIRST_RULE_TYPE))
            status.setViolated(true);
        return status;
    }

    private static Status secondRule() {
        Status status = new Status();
        OrderedList<LogData> logList = logRecords.get(lastLog.getComponentName());
        int begin = 0;
        for(LogData firstLog : logList) {
            int end = 0;
            int count = 0;
            for(LogData secondLog : logList) {

                //go forward until first log time
                if(firstLog.getEpochSeconds() <= secondLog.getEpochSeconds()) {
                    if(count > SECOND_RULE_RATE) {
                        status.setViolated(true);
                    }

                    if(secondLog.getEpochSeconds() - firstLog.getEpochSeconds() > SECOND_RULE_DURATION) {
                        if(status.isViolated()) {
                            status.setBegin(begin);
                            status.setEnd(end);
                            status.setRate(count);
                            return status;
                        }
                        break;
                    }
                    if(secondLog.getType().equals(SECOND_RULE_TYPE))
                        count++;
                }
                end++;
            }
            begin++;
        }
        return status;
    }

    private static Status thirdRule() {
        Status status = new Status();
        OrderedList<LogData> logList = logRecords.get(lastLog.getComponentName());
        int begin = 0;
        for(LogData firstLog : logList) {
            int end = 0;
            int count = 0;
            for(LogData secondLog : logList) {

                //go forward until first log time
                if(firstLog.getEpochSeconds() <= secondLog.getEpochSeconds()) {
                    if(count > THIRD_RULE_RATE) {
                        status.setViolated(true);
                    }

                    if(secondLog.getEpochSeconds() - firstLog.getEpochSeconds() > THIRD_RULE_DURATION) {
                        if(status.isViolated()) {
                            status.setBegin(begin);
                            status.setEnd(end);
                            status.setRate(count);
                            return status;
                        }
                        break;
                    }

                    count++;
                }
                end++;
            }
            begin++;
        }
        return status;
    }
}
