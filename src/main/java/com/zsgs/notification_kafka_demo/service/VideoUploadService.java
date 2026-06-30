package com.zsgs.notification_kafka_demo.service;

import com.zsgs.notification_kafka_demo.dto.VideoUploadRequestDto;
import com.zsgs.notification_kafka_demo.event.VideoUploadEvent;
import com.zsgs.notification_kafka_demo.kafka.VideoUploadProducerService;
import com.zsgs.notification_kafka_demo.model.User;
import com.zsgs.notification_kafka_demo.model.Video;
import com.zsgs.notification_kafka_demo.repository.UserRepository;
import com.zsgs.notification_kafka_demo.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoUploadService {

    private static final Logger log = LoggerFactory.getLogger(VideoUploadService.class);

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoUploadProducerService producerService;

    // Constructor Injection
    public VideoUploadService(VideoRepository videoRepository,
                              UserRepository userRepository,
                              VideoUploadProducerService producerService) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.producerService = producerService;
    }

    /**
     * Persists the exact structure of your Video domain entity
     * and streams a notification payload downstream via Kafka.
     */
    @Transactional
    public Video uploadVideo(VideoUploadRequestDto requestDto) {
        log.info("Processing video creation request: {}", requestDto.getVideoTitle());

        // 1. Fetch the creator's user entity to resolve their channel details
        User creator = null;
        try {
            creator = (User) userRepository.findById(requestDto.getCreatorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Channel creator not found with email: " + requestDto.getCreatorId()
                    ));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        String generatedVideoId = UUID.randomUUID().toString();

        // 2. Build the Video entity strictly matching your structural properties
        Video videoEntity = Video.builder()
                .videoId(generatedVideoId)
                .videoName(requestDto.getVideoTitle()) // Maps request string directly to your videoName field
                .userId(creator)                       // Binds the full User relational mapping
                .build();

        // Persist video metadata to database
        Video savedVideo = (Video) videoRepository.save(videoEntity);
        log.info("Saved video metadata successfully to DB with ID: {}", generatedVideoId);

        // 3. Fire the custom Event payload directly to your Kafka cluster broker
        VideoUploadEvent outboundEvent = new VideoUploadEvent(
                savedVideo.getVideoId(),
                savedVideo.getVideoName(), // Uses your precise field naming convention
                creator.getUserId(),       // maps to the email field inside User
                creator.getChannelName()  ,
                LocalDateTime.now()// tracks the descriptive channel title
        );

        // Handoff message execution to your Kafka template producer service pipeline
        producerService.publishVideoUpload(outboundEvent);
        log.info("Dispatched notification streaming transaction event to Kafka brokers.");

        return savedVideo;
    }
}