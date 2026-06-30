package com.zsgs.notification_kafka_demo.controller;

import com.zsgs.notification_kafka_demo.dto.VideoUploadRequestDto;
import com.zsgs.notification_kafka_demo.model.Video;
import com.zsgs.notification_kafka_demo.service.VideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    VideoUploadService videoUploadService ;

    @PostMapping("/upload")
    public ResponseEntity<?>uploadVideo(@RequestBody VideoUploadRequestDto request){

        try{
            Video uploaded =videoUploadService.uploadVideo(request);

            return ResponseEntity.ok(uploaded);


        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
