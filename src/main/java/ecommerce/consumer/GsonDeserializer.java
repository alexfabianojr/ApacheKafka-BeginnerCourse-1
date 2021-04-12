package ecommerce.consumer;

import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {
    public static final String TYPE_CONFIG = "ecommerce.consumer.type_config";
    private Class<T> type;

    @Override
    public T deserialize(String s, byte[] bytes) {
        return new GsonBuilder().create().fromJson(new String(bytes), type);
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for deserialization does not exist in the classpath");
        }
    }
}
