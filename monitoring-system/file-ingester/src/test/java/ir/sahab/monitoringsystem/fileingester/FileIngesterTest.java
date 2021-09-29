package ir.sahab.monitoringsystem.fileingester;

import ir.sahab.monitoringsystem.fileingester.config.ApplicationProperties;

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

public class FileIngesterTest {

    private final String AVAILABLE_LOG_TYPES_URI = ApplicationProperties.getProperty("random.available.log.type");
    private final String AVAILABLE_LOG_MESSAGES_URI = ApplicationProperties.getProperty("random.available.log.message");
    private final String TEST_DIRECTORY = ApplicationProperties.getProperty("test.directory");

    private final int NUMBER_OF_RANDOM_LOG_FILES = Integer.parseInt(ApplicationProperties.getProperty("random.log.number"));
    private final int MAXIMUM_NUMBER_OF_LINES = Integer.parseInt(ApplicationProperties.getProperty("random.log.maximum.line.number"));
    private final int NUMBER_OF_COMPONENTS = Integer.parseInt(ApplicationProperties.getProperty("random.log.component.number"));

    private final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final LocalDate START_DATE = LocalDate.of(Integer.parseInt(ApplicationProperties.getProperty("random.date.start.year")),
                                                      Integer.parseInt(ApplicationProperties.getProperty("random.date.start.month")),
                                                      Integer.parseInt(ApplicationProperties.getProperty("random.date.start.day")));

    private final LocalDate END_DATE = LocalDate.of(Integer.parseInt(ApplicationProperties.getProperty("random.date.end.year")),
                                                    Integer.parseInt(ApplicationProperties.getProperty("random.date.end.month")),
                                                    Integer.parseInt(ApplicationProperties.getProperty("random.date.end.day")));

    private final LocalTime START_TIME = LocalTime.of(Integer.parseInt(ApplicationProperties.getProperty("random.time.start.hour")),
                                                      Integer.parseInt(ApplicationProperties.getProperty("random.time.start.minute")),
                                                      Integer.parseInt(ApplicationProperties.getProperty("random.time.start.second")));

    private final LocalTime END_TIME = LocalTime.of(Integer.parseInt(ApplicationProperties.getProperty("random.time.end.hour")),
                                                    Integer.parseInt(ApplicationProperties.getProperty("random.time.end.minute")),
                                                    Integer.parseInt(ApplicationProperties.getProperty("random.time.end.second")));

    private List<String> availableLogTypes;
    private List<String> availableLogMessages;

    public void generateRandomTestInput() {
        loadAvailableLogTypes();
        loadAvailableLogMessages();
        createTestDirectory();

        for(int i = 0; i < NUMBER_OF_RANDOM_LOG_FILES; i++) {
            String randomFileName = generateRandomFileName();
            String logFileUri = TEST_DIRECTORY + randomFileName;
            createLogFile(logFileUri);
            fillLogFile(logFileUri);
        }
    }

    private void loadAvailableLogTypes() {
        try {
            Path availableLogTypesPath = Paths.get(AVAILABLE_LOG_TYPES_URI);
            availableLogTypes = Files.readAllLines(availableLogTypesPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOAD_AVAILABLE_LOG_TYPES METHOD.");
        }
    }

    private void loadAvailableLogMessages() {
        try {
            Path availableLogMessagesPath = Paths.get(AVAILABLE_LOG_MESSAGES_URI);
            availableLogMessages = Files.readAllLines(availableLogMessagesPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOAD_AVAILABLE_LOG_MESSAGES METHOD.");
        }
    }

    private void createTestDirectory() {
        try {
            Path testPath = Paths.get(TEST_DIRECTORY);
            Files.createDirectory(testPath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN CREATE_TEST_DIRECTORY METHOD.");
        }
    }

    private String generateRandomFileName() {
        LocalDate randomLocalDate = pickRandomDateBetween(START_DATE, END_DATE);
        LocalTime randomLocalTime = pickRandomTimeBetween(START_TIME, END_TIME);

        String randomDate = randomLocalDate.toString().replace("-", "_");
        String randomTime = randomLocalTime.toString().replace(":", "_").split("[.]")[0];

        return "NODE" + RANDOM.nextInt(NUMBER_OF_COMPONENTS) + "-" + randomDate + "-" + randomTime + ".log";
    }

    private void createLogFile(String logFileUri) {
        try {
            Path logFilePath = Paths.get(logFileUri);
            Files.createFile(logFilePath);
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN CREATE_TEST_LOG_FILES METHOD.");
        }
    }

    private void fillLogFile(String logFileUri) {
        try {
            Path logFilePath = Paths.get(logFileUri);
            String randomMultipleLog = generateRandomMultipleLog();
            BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, StandardCharsets.UTF_16);
            bufferedWriter.write(randomMultipleLog);
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN WRITE_MULTIPLE_LOGS_TO_FILES METHOD.");
        }
    }

    private String generateRandomMultipleLog() {
        StringBuilder content = new StringBuilder();

        int randomNumberOfLines = RANDOM.nextInt(MAXIMUM_NUMBER_OF_LINES);
        for(int i = 0; i < randomNumberOfLines; i++) {
            String randomSingleLog = generateRandomSingleLog();
            content.append(randomSingleLog);
        }

        return content.toString();
    }

    private String generateRandomSingleLog() {

        LocalDate randomLocalDate = pickRandomDateBetween(START_DATE, END_DATE);
        LocalTime randomLocalTime = pickRandomTimeBetween(START_TIME, END_TIME);
        String randomType = pickRandomType();
        String randomMessage = pickRandomMessage();

        return randomLocalDate + " " + randomLocalTime + ",114 [TreadName] " + randomType + " package.name.ClassName - " + randomMessage + "\n";
    }

    private LocalDate pickRandomDateBetween(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = RANDOM.nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }

    private LocalTime pickRandomTimeBetween(LocalTime startTime, LocalTime endTime) {
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        int randomTime = RANDOM.nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime);
    }

    private String pickRandomType() {
        return availableLogTypes.get(RANDOM.nextInt(availableLogTypes.size()));
    }

    private String pickRandomMessage() {
        return availableLogMessages.get(RANDOM.nextInt(availableLogMessages.size()));
    }
}
