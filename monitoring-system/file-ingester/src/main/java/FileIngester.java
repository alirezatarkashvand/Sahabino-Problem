import java.io.IOException;

public class FileIngester {
    public static void main(String[] args) throws IOException {

        String logFolder = ApplicationProperties.getProperty("log-folder");
        LogFileReader logFileReader = new LogFileReader(logFolder);
        logFileReader.run();
    }
}
