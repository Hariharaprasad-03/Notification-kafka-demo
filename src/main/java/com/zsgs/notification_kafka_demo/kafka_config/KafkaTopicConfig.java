package com.zsgs.notification_kafka_demo.kafka_config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    private final String bootstrapServers = "localhost:9092";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic videoUploadEventTopic() {
        return TopicBuilder.name("video_upload_event")
                .partitions(3) // 3 partitions allow scaling up to 3 concurrent consumers in a group
                .replicas(1)   // Set to 1 for local development sandbox environments
                // --- Custom Topic Properties ---
                .config("min.insync.replicas", "1")
                .config("retention.ms", "604800000") // Retain events for exactly 7 days
                .build();
    }
}