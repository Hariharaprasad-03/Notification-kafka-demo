package com.zsgs.notification_kafka_demo.service;

import com.zsgs.notification_kafka_demo.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    SubscriptionRepository subscriptionRepository ;

    SubscriptionService(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository = subscriptionRepository ;
    }


    public List<String> getSubscribersForCreator(String creatorId) {
        return subscriptionRepository.findUserIdsByCreatorId(creatorId);
    }
}
