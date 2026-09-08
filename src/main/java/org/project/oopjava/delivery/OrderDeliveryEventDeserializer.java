package org.project.oopjava.delivery;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class OrderDeliveryEventDeserializer implements Deserializer<OrderDeliveryEvent> {
    private static final String FORMAT_CONFIG = "kafka.value.format";
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private boolean protobuf;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Object format = configs.get(FORMAT_CONFIG);
        protobuf = "protobuf".equalsIgnoreCase(String.valueOf(format == null ? "json" : format));
    }

    @Override
    public OrderDeliveryEvent deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return protobuf
                    ? OrderDeliveryEventCodec.fromProtobuf(data)
                    : objectMapper.readValue(data, OrderDeliveryEvent.class);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to deserialize delivery event", exception);
        }
    }
}
