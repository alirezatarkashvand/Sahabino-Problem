package ir.sahab.monitoringsystem.fileingester.logfilehandler;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogFileRemover implements Callback {
    private final Path logFilePath;

    public LogFileRemover(Path logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        try {
            Files.delete(logFilePath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
