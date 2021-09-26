import java.io.IOException;

public class FileIngester {
    public static void main(String[] args) throws IOException {
        String dir = "/home/alireza/Test";
        LogFileReader logFileReader = new LogFileReader(dir);
        logFileReader.run();
    }
}
