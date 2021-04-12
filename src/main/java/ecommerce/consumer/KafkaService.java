package ecommerce.consumer;

import ecommerce.producer.ConsumerFunction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;

    public KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> customKafkaProperties) {
        this(groupId, parse, type, customKafkaProperties);
        this.consumer.subscribe(Collections.singletonList(topic)); //eh raro escutar mais de um tópico
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> customKafkaProperties) {
        this(groupId, parse, type, customKafkaProperties);
        this.consumer.subscribe(topic);
    }

    private KafkaService(String groupId, ConsumerFunction parse, Class<T> type, Map<String, String> customKafkaProperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(type, groupId, customKafkaProperties));
    }

    public void run() {
        while (true) {
            ConsumerRecords<String, T> poll = consumer.poll(Duration.ofMillis(100));
            if (poll.isEmpty()) {
                System.out.println("Não encontrei registros");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            System.out.println("Consumindo, data: " + LocalDateTime.now());
            for (var record : poll) {
                parse.consume(record);
            }
        }
    }

    private Properties properties(Class<T> type, String groupId, Map<String, String> customKafkaProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); //Para processar de 1 a 1 a fila (melhora o paralelismo)
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(customKafkaProperties); // novas configs ou reescrever configs antigas
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
