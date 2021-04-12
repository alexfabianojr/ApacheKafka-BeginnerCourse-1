package ecommerce.producer;

import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {
    @Override
    public byte[] serialize(String s, T object) {
        return new GsonBuilder().create().toJson(object).getBytes();
    }
}
