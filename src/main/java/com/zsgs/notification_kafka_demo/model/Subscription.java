package com.zsgs.notification_kafka_demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {

    @Id
    @Column(name = "id")
    private String subscriptionId ;

    @Column(name = "subscriber_id")
    private String subscriberId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="channel_id",
    referencedColumnName = "email",
    foreignKey = @ForeignKey(name = "user_id_fk_suscription"))
    private User channelId ;
}
