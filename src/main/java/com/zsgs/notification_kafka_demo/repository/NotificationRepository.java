package com.zsgs.notification_kafka_demo.repository;

import com.zsgs.notification_kafka_demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,String> {
}
