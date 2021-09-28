package ir.sahab.monitoringsystem.fileingester.logfilehandler;

import ir.sahab.monitoringsystem.fileingester.common.State;
import ir.sahab.monitoringsystem.fileingester.kafkaclient.KafkaClient;
import org.apache.kafka.clients.producer.Callback;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class LogFileReader implements Runnable{
    private final WatchService watchService = FileSystems.getDefault().newWatchService();;
    private final Path logFolderPath;

    private State state;

    public LogFileReader(String logFolder) throws IOException {
        this.logFolderPath = Paths.get(logFolder);
        this.logFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        this.state = State.RUNNING;
    }

    public void stop() {
        try {
            this.state = State.SHUTDOWN;
            this.watchService.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        while(state == State.RUNNING) {
            read();
            watch();
        }
    }

    private void watch() {
        try {
            WatchKey key = watchService.take();
            key.pollEvents();
            key.reset();
        } catch (ClosedWatchServiceException e) {
            System.out.println("System Shutdown.");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void read()  {
        try {
            List<Path> paths = readFilePaths();
            System.out.println(paths);
            for(Path path: paths) {
                String key = splitComponentName(getFileName(path));
                String value = concatenate(key, Files.readAllLines(path));
                sendToKafka(key, value, new LogFileRemover(path));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Path> readFilePaths() throws IOException {
        return Files.list(logFolderPath)
                    .filter(path -> path.toString().endsWith(".log"))
                    .collect(Collectors.toList());
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private String splitComponentName(String fileName) {
        int COMPONENT_NAME_LOCATION = 0;
        return fileName.split("-")[COMPONENT_NAME_LOCATION];
    }

    private String concatenate(String name, List<String> lines) {
        String specialDelimiter = "$$";
        StringBuilder result = new StringBuilder(name);

        for(String line: lines)
            result.append(specialDelimiter).append(line);

        return result.toString();
    }

    private void sendToKafka(String key, String value, Callback callback) {
        KafkaClient<String, String> kafkaClient = new KafkaClient<>();
        kafkaClient.send(key, value, callback);
    }
}
