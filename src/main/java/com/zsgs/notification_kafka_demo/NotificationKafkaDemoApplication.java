package com.zsgs.notification_kafka_demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotificationKafkaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationKafkaDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(
			com.zsgs.notification_kafka_demo.repository.UserRepository userRepository,
			com.zsgs.notification_kafka_demo.repository.SubscriptionRepository subscriptionRepository) {

		return new CommandLineRunner() {
			@Override
			@jakarta.transaction.Transactional
			public void run(String... args) throws Exception {
				System.out.println("=== Starting Database Initialization ===");

				// 1. Create User 1 (The Channel Creator)
				com.zsgs.notification_kafka_demo.model.User user1 = com.zsgs.notification_kafka_demo.model.User.builder()
						.userId("user1@example.com")
						.userName("Alice Smith")
						.channelName("Alice Tech Chronicles")
						.subscriptions(new java.util.ArrayList<>())
						.videos(new java.util.ArrayList<>())
						.notifications(new java.util.ArrayList<>())
						.build();

				// 2. Create User 2 (Subscriber)
				com.zsgs.notification_kafka_demo.model.User user2 = com.zsgs.notification_kafka_demo.model.User.builder()
						.userId("user2@example.com")
						.userName("Bob Jones")
						.channelName("Bob's Daily Vlogs")
						.subscriptions(new java.util.ArrayList<>())
						.videos(new java.util.ArrayList<>())
						.notifications(new java.util.ArrayList<>())
						.build();

				// 3. Create User 3 (Subscriber)
				com.zsgs.notification_kafka_demo.model.User user3 = com.zsgs.notification_kafka_demo.model.User.builder()
						.userId("user3@example.com")
						.userName("Charlie Brown")
						.channelName("Charlie Cooks")
						.subscriptions(new java.util.ArrayList<>())
						.videos(new java.util.ArrayList<>())
						.notifications(new java.util.ArrayList<>())
						.build();

				// Save users first so they exist in the persistence context
				userRepository.save(user1);
				userRepository.save(user2);
				userRepository.save(user3);

				// 4. Create Subscription: User 2 subscribes to User 1
				com.zsgs.notification_kafka_demo.model.Subscription sub1 = com.zsgs.notification_kafka_demo.model.Subscription.builder()
						.subscriptionId(java.util.UUID.randomUUID().toString())
						.subscriberId(user2.getUserId())
						.channelId(user1)
						.build();

				// 5. Create Subscription: User 3 subscribes to User 1
				com.zsgs.notification_kafka_demo.model.Subscription sub2 = com.zsgs.notification_kafka_demo.model.Subscription.builder()
						.subscriptionId(java.util.UUID.randomUUID().toString())
						.subscriberId(user3.getUserId())
						.channelId(user1)
						.build();

				// Synchronize the bidirectional memory state
				user1.getSubscriptions().add(sub1);
				user1.getSubscriptions().add(sub2);

				// Persist the relationships
				subscriptionRepository.save(sub1);
				subscriptionRepository.save(sub2);

				System.out.println("=== Database Initialization Complete ===");
			}
		};
	}

}
