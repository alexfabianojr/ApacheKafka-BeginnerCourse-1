package ecommerce.consumer;

import ecommerce.entidade.Email;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/** CONSUMER * */
public final class EmailService {
    public static void main(String[] args) {

        var emailService = new EmailService();

        try (var service = new KafkaService(EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL", emailService::parse, Email.class, Map.of())) {

            service.run();
        }

    }

    void parse(ConsumerRecord<String, Email> record) {
        System.out.println("<------------------------------------->");
        System.out.println("Sending email!");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
    }
}
