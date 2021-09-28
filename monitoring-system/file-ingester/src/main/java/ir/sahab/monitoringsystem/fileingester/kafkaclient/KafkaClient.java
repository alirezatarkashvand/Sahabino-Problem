package ir.sahab.monitoringsystem.fileingester.kafkaclient;

import ir.sahab.monitoringsystem.fileingester.config.ApplicationProperties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KafkaClient<K, V> {
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

    private final Producer<K, V> producer;

    public KafkaClient(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ApplicationProperties.getProperty("bootstrap.servers"));
        properties.put("acks", ApplicationProperties.getProperty("acks"));
        properties.put("retries", ApplicationProperties.getProperty("retries"));
        properties.put("linger.ms", ApplicationProperties.getProperty("linger.ms"));
        properties.put("key.serializer", ApplicationProperties.getProperty("key.serializer"));
        properties.put("value.serializer", ApplicationProperties.getProperty("value.serializer"));
        this.producer = new KafkaProducer<>(properties);
    }

    public void send(K key, V value, Callback callback) {
        ProducerRecord<K, V> record = new ProducerRecord<>(ApplicationProperties.getProperty("topic-name"), key, value);
        producer.send(record, callback);
    }
}
