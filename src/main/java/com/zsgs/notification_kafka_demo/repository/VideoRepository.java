package com.zsgs.notification_kafka_demo.repository;

import com.zsgs.notification_kafka_demo.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video,String> {
}
