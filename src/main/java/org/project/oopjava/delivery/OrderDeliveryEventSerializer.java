package org.project.oopjava.delivery;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class OrderDeliveryEventSerializer implements Serializer<OrderDeliveryEvent> {
    private static final String FORMAT_CONFIG = "kafka.value.format";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private boolean protobuf;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Object format = configs.get(FORMAT_CONFIG);
        protobuf = "protobuf".equalsIgnoreCase(String.valueOf(format == null ? "json" : format));
    }

    @Override
    public byte[] serialize(String topic, OrderDeliveryEvent event) {
        if (event == null) {
            return null;
        }
        try {
            return protobuf ? OrderDeliveryEventCodec.toProtobuf(event) : objectMapper.writeValueAsBytes(event);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to serialize delivery event", exception);
        }
    }
}
