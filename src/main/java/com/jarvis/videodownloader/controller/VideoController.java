package com.jarvis.videodownloader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jarvis.videodownloader.model.VideoInfo;
import com.jarvis.videodownloader.service.VideoService;

@RestController
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/fetch")
    public VideoInfo fetchVideo(@RequestParam("url") String url) throws Exception {

        return videoService.fetchVideoInfo(url);

    }

}