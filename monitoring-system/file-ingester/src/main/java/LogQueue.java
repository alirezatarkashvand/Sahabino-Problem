import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class LogQueue {

    private static final LinkedBlockingQueue<Pair<String, List<String>>> logQueue = new LinkedBlockingQueue<>();;

    private LogQueue() {}

    public static LinkedBlockingQueue<Pair<String, List<String>>>  getInstance() {
        return logQueue;
    }

    public void add(Pair<String, List<String>> rawLog) {
        logQueue.add(rawLog);
    }

    public Pair<String, List<String >> take() throws InterruptedException {
        return logQueue.take();
    }
}
