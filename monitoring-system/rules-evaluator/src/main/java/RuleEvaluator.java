import java.util.Map;

public class RuleEvaluator {
    public static void main(String[] args) {
        KafkaConsumerClient<String, String> kafkaConsumerClient = new KafkaConsumerClient<>();
        while(true) {
            Map<String, String> received_data = kafkaConsumerClient.receive();
            System.out.println(received_data);
        }
    }
}
