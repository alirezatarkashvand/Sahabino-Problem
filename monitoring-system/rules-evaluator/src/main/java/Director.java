import org.javatuples.Pair;

import java.util.List;

public class Director implements Runnable{

    private State state = State.RUNNING;

    public void stop() {
        this.state = State.SHUTDOWN;
        KafkaConsumerClient.close();
    }

    @Override
    public void run() {

        while (state == State.RUNNING) {
            List<Pair<String, String>> logFileList = KafkaConsumerClient.receive();
            List<LogData> logDataList = LogParser.parse(logFileList);
            for(LogData logData : logDataList) {
                Analyzer.updateWith(logData);
                List<Warning> warningList = Analyzer.analyze();
                for(Warning warning : warningList)
                    MySQLClient.insert(warning);
            }
        }
    }
}
