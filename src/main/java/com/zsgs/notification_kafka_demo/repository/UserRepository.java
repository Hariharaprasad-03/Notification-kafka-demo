package com.zsgs.notification_kafka_demo.repository;

import com.zsgs.notification_kafka_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
