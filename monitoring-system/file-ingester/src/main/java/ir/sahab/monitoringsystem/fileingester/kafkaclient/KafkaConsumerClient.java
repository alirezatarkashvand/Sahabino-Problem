package ir.sahab.monitoringsystem.fileingester.kafkaclient;

import ir.sahab.monitoringsystem.fileingester.config.ApplicationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.javatuples.Pair;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerClient<K, V> {

    private final Consumer<K,  V> consumer;

    public KafkaConsumerClient() {
        Properties properties = new Properties();
        properties.put("group.id", ApplicationProperties.getProperty("group.id"));
        properties.put("enable.auto.commit", ApplicationProperties.getProperty("enable.auto.commit"));
        properties.put("auto.commit.interval.ms", ApplicationProperties.getProperty("auto.commit.interval.ms"));
        properties.put("session.timeout.ms", ApplicationProperties.getProperty("session.timeout.ms"));
        properties.put("key.deserializer", ApplicationProperties.getProperty("key.deserializer"));
        properties.put("value.deserializer", ApplicationProperties.getProperty("value.deserializer"));
        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(List.of(ApplicationProperties.getProperty("topic.name")));
    }

    public List<Pair<K, V>> receive() {
        List<Pair<K, V>> recordsList = new ArrayList<>();

        ConsumerRecords<K, V> records = consumer.poll(Duration.ofMinutes(1));

        for(ConsumerRecord<K, V> record : records) {
            Pair<K, V> keyValuePair = new Pair<>(record.key(), record.value());
            recordsList.add(keyValuePair);
        }

        return recordsList;
    }
}
