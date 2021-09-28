package ir.sahab.monitoringsystem.fileingester;

import ir.sahab.monitoringsystem.fileingester.config.ApplicationProperties;
import ir.sahab.monitoringsystem.fileingester.logfilehandler.LogFileReader;

import java.io.IOException;
import java.util.Scanner;

public class FileIngester {

    private static LogFileReader instantiateLogFileReader() throws IOException {
        String logFolder = ApplicationProperties.getProperty("log-folder");
        return new LogFileReader(logFolder);
    }

    public static void runLogFileReader(LogFileReader logFileReader) {
        Thread thread = new Thread(logFileReader);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private static void shutdownByUserCommand(LogFileReader runningLogFileReader) {
        String input = "";
        Scanner scanner = new Scanner(System.in);
        while(!input.equals("shutdown")) {
            input = scanner.next();
        }
        runningLogFileReader.stop();
    }

    public static void main(String[] args) {
        try {
            LogFileReader logFileReader = instantiateLogFileReader();
            runLogFileReader(logFileReader);
            shutdownByUserCommand(logFileReader);
        } catch (IOException e) {
            System.out.println("LOG FOLDER NOT FOUND.");
        }
    }
}
