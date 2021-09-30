package ir.sahab.monitoringsystem.loggenerator;

import ir.sahab.monitoringsystem.loggenerator.config.ApplicationProperties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LogGenerator {

    private static final String AVAILABLE_LOG_TYPES_URI = ApplicationProperties.getProperty("random.available.log.type");
    private static final String AVAILABLE_LOG_MESSAGES_URI = ApplicationProperties.getProperty("random.available.log.message");
    private static final String TEST_DIRECTORY = ApplicationProperties.getProperty("test.directory");

    private static final int NUMBER_OF_RANDOM_LOG_FILES = Integer.parseInt(ApplicationProperties.getProperty("random.log.number"));
    private static final int MAXIMUM_NUMBER_OF_LINES = Integer.parseInt(ApplicationProperties.getProperty("random.log.maximum.line.number"));
    private static final int NUMBER_OF_COMPONENTS = Integer.parseInt(ApplicationProperties.getProperty("random.log.component.number"));

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final LocalDate START_DATE = LocalDate.of(Integer.parseInt(ApplicationProperties.getProperty("random.date.start.year")),
                                                             Integer.parseInt(ApplicationProperties.getProperty("random.date.start.month")),
                                                             Integer.parseInt(ApplicationProperties.getProperty("random.date.start.day")));

    private static final LocalDate END_DATE = LocalDate.of(Integer.parseInt(ApplicationProperties.getProperty("random.date.end.year")),
                                                           Integer.parseInt(ApplicationProperties.getProperty("random.date.end.month")),
                                                           Integer.parseInt(ApplicationProperties.getProperty("random.date.end.day")));

    private static final LocalTime START_TIME = LocalTime.of(Integer.parseInt(ApplicationProperties.getProperty("random.time.start.hour")),
                                                             Integer.parseInt(ApplicationProperties.getProperty("random.time.start.minute")),
                                                             Integer.parseInt(ApplicationProperties.getProperty("random.time.start.second")));

    private static final LocalTime END_TIME = LocalTime.of(Integer.parseInt(ApplicationProperties.getProperty("random.time.end.hour")),
                                                           Integer.parseInt(ApplicationProperties.getProperty("random.time.end.minute")),
                                                           Integer.parseInt(ApplicationProperties.getProperty("random.time.end.second")));

    private static List<String> availableLogTypes;
    private static List<String> availableLogMessages;

    private static void generateRandomTestInput() {
        loadAvailableLogTypes();
        loadAvailableLogMessages();
        createTestDirectory();

        for(int i = 0; i < NUMBER_OF_RANDOM_LOG_FILES; i++) {
            String randomFileName = generateRandomFileName();
            String logFileUri = TEST_DIRECTORY + randomFileName;
            createLogFile(logFileUri);
            String randomNumberOfLogs = generateRandomNumberOfLogs();
            writeInto(logFileUri, randomNumberOfLogs);
        }
    }

    private static void loadAvailableLogTypes() {
        try {
            Path availableLogTypesPath = Paths.get(AVAILABLE_LOG_TYPES_URI);
            availableLogTypes = Files.readAllLines(availableLogTypesPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOAD_AVAILABLE_LOG_TYPES METHOD.");
        }
    }

    private static void loadAvailableLogMessages() {
        try {
            Path availableLogMessagesPath = Paths.get(AVAILABLE_LOG_MESSAGES_URI);
            availableLogMessages = Files.readAllLines(availableLogMessagesPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOAD_AVAILABLE_LOG_MESSAGES METHOD.");
        }
    }

    private static void createTestDirectory() {
        try {
            Path testPath = Paths.get(TEST_DIRECTORY);
            if(Files.notExists(testPath))
                Files.createDirectory(testPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN CREATE_TEST_DIRECTORY METHOD.");
        }
    }

    private static String generateRandomFileName() {
        LocalDate randomLocalDate = pickRandomDate();
        LocalTime randomLocalTime = pickRandomTime();

        String randomDate = randomLocalDate.toString().replace("-", "_");
        String randomTime = randomLocalTime.toString().replace(":", "_").split("[.]")[0];

        return "NODE" + RANDOM.nextInt(NUMBER_OF_COMPONENTS) + "-" + randomDate + "-" + randomTime + ".log";
    }

    private static void createLogFile(String logFileUri) {
        try {
            Path logFilePath = Paths.get(logFileUri);
            if(Files.notExists(logFilePath))
                Files.createFile(logFilePath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN CREATE_TEST_LOG_FILES METHOD.");
        }
    }

    private static void writeInto(String uri, String content) {
        try {
            Path logFilePath = Paths.get(uri);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, StandardCharsets.UTF_8);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN WRITE_MULTIPLE_LOGS_TO_FILES METHOD.");
        }
    }

    private static String generateRandomNumberOfLogs() {
        StringBuilder content = new StringBuilder();

        int randomNumberOfLines = RANDOM.nextInt(MAXIMUM_NUMBER_OF_LINES);
        for(int i = 0; i < randomNumberOfLines; i++) {
            String randomSingleLog = generateRandomSingleLog();
            content.append(randomSingleLog);
        }

        return content.toString();
    }

    private static String generateRandomSingleLog() {

        LocalDate randomLocalDate = pickRandomDate();
        LocalTime randomLocalTime = pickRandomTime();
        String randomType = pickRandomType();
        String randomMessage = pickRandomMessage();

        return randomLocalDate + " " + randomLocalTime + ",114 [TreadName] " + randomType + " package.name.ClassName - " + randomMessage + "\n";
    }

    private static LocalDate pickRandomDate() {
        long startEpochDay = LogGenerator.START_DATE.toEpochDay();
        long endEpochDay = LogGenerator.END_DATE.toEpochDay();
        long randomDay = RANDOM.nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    private static LocalTime pickRandomTime() {
        int startSeconds = LogGenerator.START_TIME.toSecondOfDay();
        int endSeconds = LogGenerator.END_TIME.toSecondOfDay();
        int randomTime = RANDOM.nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime);
    }

    private static String pickRandomType() {
        return availableLogTypes.get(RANDOM.nextInt(availableLogTypes.size()));
    }

    private static String pickRandomMessage() {
        return availableLogMessages.get(RANDOM.nextInt(availableLogMessages.size()));
    }

    public static void main(String[] args) {
        generateRandomTestInput();
    }
}
