package com.zsgs.notification_kafka_demo.kafka_config;


import com.zsgs.notification_kafka_demo.event.VideoUploadEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final String bootstrapServers = "localhost:9092";

    @Bean
    public ProducerFactory<String, VideoUploadEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        // --- Custom Producer Performance & Reliability Props ---
        // Ensuring strict at-least-once delivery guarantees
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);

        // Performance Optimization: Batching and compression profiles
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10); // Wait 10ms for records to arrive before sending batch
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768); // 32KB batch size
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"); // Balance between CPU and network compression ratio

        // Disable type headers to keep payload simple across external boundaries
        JacksonJsonSerializer<VideoUploadEvent> jsonSerializer = new JacksonJsonSerializer<>();
        jsonSerializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), jsonSerializer);
    }

    @Bean
    public KafkaTemplate<String, VideoUploadEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}