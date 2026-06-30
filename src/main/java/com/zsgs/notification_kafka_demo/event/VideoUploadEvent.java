package com.zsgs.notification_kafka_demo.event;


import java.io.Serializable;
import java.time.LocalDateTime;

public class VideoUploadEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String videoId;
    private String title;
    private String creatorId;
    private String creatorName;
    private LocalDateTime uploadedAt;

    // Default constructor needed for JSON deserialization
    public VideoUploadEvent() {}

    public VideoUploadEvent(String videoId, String title, String creatorId, String creatorName, LocalDateTime uploadedAt) {
        this.videoId = videoId;
        this.title = title;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.uploadedAt = uploadedAt;
    }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    @Override
    public String toString() {
        return "VideoUploadEvent{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}