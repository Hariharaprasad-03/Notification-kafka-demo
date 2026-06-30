package com.zsgs.notification_kafka_demo.kafka;

import com.zsgs.notification_kafka_demo.event.VideoUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class VideoUploadProducerService {

    private static final Logger log = LoggerFactory.getLogger(VideoUploadProducerService.class);
    private static final String TOPIC = "video_upload_event";

    private final KafkaTemplate<String, VideoUploadEvent> kafkaTemplate;

    public VideoUploadProducerService(KafkaTemplate<String, VideoUploadEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishVideoUpload(VideoUploadEvent event) {
        log.info("Preparing to dispatch video upload event: {}", event.getVideoId());

        // Using creatorId as partition key ensures in-order delivery per creator
        CompletableFuture<SendResult<String, VideoUploadEvent>> future =
                kafkaTemplate.send(TOPIC, event.getCreatorId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Successfully produced event to topic [{}] partition [{}] at offset [{}]",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Critical failure producing event for video id [{}]. Error: ",
                        event.getVideoId(), ex);
            }
        });
    }
}

