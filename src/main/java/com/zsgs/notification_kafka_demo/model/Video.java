package com.zsgs.notification_kafka_demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "video")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {

    @Id
    @Column(name = "video_id")
    private String videoId ;

    @Column(name = "video_name")
    private String videoName ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "creator_id",
    referencedColumnName = "email",
    foreignKey = @ForeignKey(name = "user_id_fk")
    )
    @JsonBackReference
    private User userId ;


}
