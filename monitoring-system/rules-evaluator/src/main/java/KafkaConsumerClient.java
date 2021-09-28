import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerClient<K, V> {
    //Initialize Log4j
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("/home/alireza/Sahabino-Problem/monitoring-system/file-ingester/src/main/resources/log4j.properties"));
            PropertyConfigurator.configure(properties);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private final Consumer<K, V> consumer;

    public KafkaConsumerClient(){
        Properties properties = new Properties();
        this.consumer = new KafkaConsumer<>(properties);
    }

    public void receive() {
        consumer.subscribe(List.of("topic"));
    }
}
