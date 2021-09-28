import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.List;
import java.util.Properties;

public class KafkaClient {

    private static final Producer<String, String> producer;

    static {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ApplicationProperties.getProperty("bootstrap.servers"));
        properties.put("acks", ApplicationProperties.getProperty("acks"));
        properties.put("retries", ApplicationProperties.getProperty("retries"));
        properties.put("linger.ms", ApplicationProperties.getProperty("linger.ms"));
        properties.put("key.serializer", ApplicationProperties.getProperty("key.serializer"));
        properties.put("value.serializer", ApplicationProperties.getProperty("value.serializer"));
        producer = new KafkaProducer<>(properties);
    }

    public static void send(String key, String value, Callback callback) {
        ProducerRecord<String, String> record = new ProducerRecord<>(ApplicationProperties.getProperty("topic-name"), key, value);
        producer.send(record, callback);
    }
}
