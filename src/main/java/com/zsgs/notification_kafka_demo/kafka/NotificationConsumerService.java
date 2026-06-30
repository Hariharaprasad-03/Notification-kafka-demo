package com.zsgs.notification_kafka_demo.kafka;

import com.zsgs.notification_kafka_demo.event.VideoUploadEvent;
import com.zsgs.notification_kafka_demo.model.Notification;
import com.zsgs.notification_kafka_demo.model.User;
import com.zsgs.notification_kafka_demo.repository.NotificationRepository;
import com.zsgs.notification_kafka_demo.service.SubscriptionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationConsumerService {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumerService.class);

    private final SubscriptionService subscriptionService;
    private final NotificationRepository notificationRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public NotificationConsumerService(SubscriptionService subscriptionService,
                                       NotificationRepository notificationRepository,
                                       EntityManager entityManager) {
        this.subscriptionService = subscriptionService;
        this.notificationRepository = notificationRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    @KafkaListener(
            topics = "video_upload_event",
            groupId = "notification-system-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void processVideoUploadNotification(
            @Payload VideoUploadEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("[Kafka] Partition: {} | Offset: {} processing.", partition, offset);

        try {
            // 1. Fetch the target list of subscriber userIds (emails) from your join infrastructure
            List<String> subscriberEmails = subscriptionService.getSubscribersForCreator(event.getCreatorId());

            if (subscriberEmails.isEmpty()) {
                log.info("No active subscribers found for channel: {}", event.getCreatorName());
                return;
            }

            // Using the newly modified event fields to construct the descriptive output
            String notificationMessage = String.format("Channel '%s' uploaded a new video: %s",
                    event.getCreatorName(), event.getTitle());

            // 2. Map target entity collections utilizing lazy proxy initialization mechanics
            List<Notification> notificationsToSave = subscriberEmails.stream()
                    .map(email -> {
                        // Hibernate will treat 'email' as the Primary Key value for the User proxy instance
                        // mapping to the underlying string field definition inside your User entity
                        User userProxy = entityManager.getReference(User.class, email);

                        return Notification.builder()
                                .notificationId(UUID.randomUUID().toString())
                                .userId(userProxy) // Ties relationship field to the User proxy target reference
                                .message(notificationMessage)
                                .build();
                    })
                    .collect(Collectors.toList());

            // 3. Complete persistence processing context execution boundaries
            notificationRepository.saveAll(notificationsToSave);
            log.info("Successfully dispatched and saved {} notification records for channel: {}",
                    notificationsToSave.size(), event.getCreatorName());

        } catch (Exception e) {
            log.error("Failed to commit persistent message pipeline processing context: ", e);
            throw e;
        }
    }
}