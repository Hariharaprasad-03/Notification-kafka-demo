package com.zsgs.notification_kafka_demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name ="email" )
    private String userId ;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "channel_name")
    private String channelName ;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL)
    private List<Video> videos ;

    @OneToMany(mappedBy = "channelId",cascade =  CascadeType.ALL)

    private List<Subscription>subscriptions;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL)
    private List<Notification>notifications ;
}
