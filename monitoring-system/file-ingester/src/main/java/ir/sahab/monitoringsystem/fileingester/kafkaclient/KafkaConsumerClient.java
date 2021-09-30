package ir.sahab.monitoringsystem.fileingester.kafkaclient;

import ir.sahab.monitoringsystem.fileingester.config.ApplicationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumerClient<K, V> {

    private final Consumer<K,  V> consumer;

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
        return recordsHashMap;
    }
}
