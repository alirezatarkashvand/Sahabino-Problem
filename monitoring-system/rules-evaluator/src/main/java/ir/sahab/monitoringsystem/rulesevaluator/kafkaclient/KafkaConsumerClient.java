package ir.sahab.monitoringsystem.rulesevaluator.kafkaclient;

import ir.sahab.monitoringsystem.rulesevaluator.config.ApplicationProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.PropertyConfigurator;
import org.javatuples.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class KafkaConsumerClient {
    //Initialize Log4j
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("/home/alireza/Sahabino-Problem/monitoring-system/rules-evaluator/src/main/resources/log4j.properties"));
            PropertyConfigurator.configure(properties);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final Consumer<String, String> consumer;

    static {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ApplicationProperties.getProperty("bootstrap.servers"));
        properties.put("group.id", ApplicationProperties.getProperty("group.id"));
        properties.put("enable.auto.commit", ApplicationProperties.getProperty("enable.auto.commit"));
        properties.put("auto.commit.interval.ms", ApplicationProperties.getProperty("auto.commit.interval.ms"));
        properties.put("session.timeout.ms", ApplicationProperties.getProperty("session.timeout.ms"));
        properties.put("key.deserializer", ApplicationProperties.getProperty("key.deserializer"));
        properties.put("value.deserializer", ApplicationProperties.getProperty("value.deserializer"));
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(List.of(ApplicationProperties.getProperty("topic.name")));
    }

    public static List<Pair<String, String>> receive() {
        List<Pair<String, String>> recordsList = new ArrayList<>();

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(20));
        records.forEach(record -> recordsList.add(new Pair<>(record.key(), record.value())));

        return recordsList;
    }

    public static void close() {
        consumer.close();
    }
}
