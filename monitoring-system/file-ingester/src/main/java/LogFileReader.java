import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class LogFileReader {
    private final WatchService watchService;
    private final Path logFolderPath;

    public LogFileReader(String logFolder) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.logFolderPath = Paths.get(logFolder);
        this.logFolderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
    }

    public void run() throws IOException {
        try {
            while(true) {
                read();
                watch();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            watchService.close();
        }
    }

    private void watch() throws InterruptedException {
        WatchKey key = watchService.take();
        key.pollEvents();
        key.reset();
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

    private void read() throws IOException {
        List<Path> paths = readFilePaths();
        System.out.println(paths);
        for(Path p: paths) {
            String fileName = getFileName(p);
            String componentName = splitComponentName(fileName);
            List<String> logs = Files.readAllLines(p);
            System.out.println(componentName + " " + logs.get(0));
        }
    }
}
