package com.zsgs.notification_kafka_demo.repository;

import com.zsgs.notification_kafka_demo.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,String> {

    @Query(value = "SELECT subscriber_id FROM subscription WHERE channel_id= :creatorId", nativeQuery = true)
    List<String> findUserIdsByCreatorId(@Param("creatorId") String creatorId);
}
