package com.zsgs.notification_kafka_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoUploadRequestDto {
    private String videoTitle;
    private String description;
    private String creatorId; // The email (userId) of the content creator uploading the video
}