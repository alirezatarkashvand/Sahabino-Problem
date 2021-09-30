import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public KafkaConsumerClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ApplicationProperties.getProperty("bootstrap.servers"));
        properties.put("group.id", ApplicationProperties.getProperty("group.id"));
        properties.put("enable.auto.commit", ApplicationProperties.getProperty("enable.auto.commit"));
        properties.put("auto.commit.interval.ms", ApplicationProperties.getProperty("auto.commit.interval.ms"));
        properties.put("session.timeout.ms", ApplicationProperties.getProperty("session.timeout.ms"));
        properties.put("key.deserializer", ApplicationProperties.getProperty("key.deserializer"));
        properties.put("value.deserializer", ApplicationProperties.getProperty("value.deserializer"));
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(List.of(ApplicationProperties.getProperty("topic.name")));
    }

    public Map<K, V> receive() {
        Map<K, V> recordsHashMap = new HashMap<>();
        ConsumerRecords<K, V> records = consumer.poll(Duration.ofSeconds(10));
        records.forEach(record -> recordsHashMap.put(record.key(), record.value()));
        for(ConsumerRecord<K, V> record:records)
            System.out.println(record.value());
        return recordsHashMap;
    }
}
