package ecommerce.consumer;

import ecommerce.entidade.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/** CONSUMER * */
public class FraudDetectorService {
    public static void main(String[] args) {

        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER", fraudService::parse, Order.class, Map.of())) {

            service.run();
        }

    }

    void parse(ConsumerRecord<String, Order> record) {
        System.out.println("Processando novas ordens, checking for frauds");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }
}
