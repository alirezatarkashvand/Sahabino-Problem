package ir.sahab.monitoringsystem.fileingester.logfilehandler;

import ir.sahab.monitoringsystem.fileingester.common.State;
import ir.sahab.monitoringsystem.fileingester.kafkaclient.KafkaProducerClient;
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
            System.err.println("IOEXCEPTION IN LOG_FILE_READER.STOP METHOD.");
        }
    }

    @Override
    public void run() {
        readPreExistingFiles();
        readSubsequentFiles();
    }

    private void readPreExistingFiles() {
        try {
            for(Path path : readFilesPath()) {
                String fileName = getFileName(path);
                String fileContent = readFileContent(path);
                sendToKafka(fileName, fileContent, new LogFileRemover(path));
            }

        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOG_FILE_READER.READ_PRE_EXISTING_FILES METHOD.");
        }
    }

    private void readSubsequentFiles() {
        try {
            WatchKey watchKey;
            while(((watchKey = watchService.take()) != null) && state==State.RUNNING) {
                for(WatchEvent<?> event: watchKey.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if(StandardWatchEventKinds.ENTRY_CREATE.equals(kind)){
                        String fileName = event.context().toString();
                        if(fileName.endsWith(".log")) {
                            Path path = Paths.get(logFolderPath.toString(), fileName);
                            String fileContent = readFileContent(path);
                            sendToKafka(fileName, fileContent, new LogFileRemover(path));
                        }
                    }
                }
                watchKey.reset();
            }
        } catch (ClosedWatchServiceException e) {
            System.out.println("SYSTEM SHUTDOWN.");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private String readFileContent(Path path)  {
        try {
            return concatenate(Files.readAllLines(path));
        } catch (IOException e) {
            System.err.println("IOEXCEPTION IN LOG_FILE_READER.READ_FILE_CONTENT METHOD.");
            return "";
        }
    }

    private List<Path> readFilesPath() throws IOException {
        return Files.list(logFolderPath)
                    .filter(path -> path.toString().endsWith(".log"))
                    .collect(Collectors.toList());
    }

    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    private String concatenate(List<String> lines) {
        StringBuilder result = new StringBuilder();
        if(lines.size() > 1) {
            String SPECIAL_DELIMITER = "$$";
            for (String line : lines)
                result.append(line).append(SPECIAL_DELIMITER);
        } else if(lines.size() == 1) {
            result.append(lines.get(0));
        }

        return result.toString();
    }

    private void sendToKafka(String key, String value, Callback callback) {
        KafkaProducerClient<String, String> kafkaProducerClient = new KafkaProducerClient<>();
        kafkaProducerClient.send(key, value, callback);
    }
}
