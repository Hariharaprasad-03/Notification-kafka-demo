package com.zsgs.notification_kafka_demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @Column(name = "notification_id")
    private String notificationId ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
    referencedColumnName = "email",
    foreignKey = @ForeignKey(name = "user_id_fk_notification"))
    private User userId ;

    @Column(name = "message")
    private String message ;
}
