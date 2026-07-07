package com.jarvis.videodownloader.controller;

import com.jarvis.videodownloader.model.DownloadProgress;
import com.jarvis.videodownloader.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/progress/{downloadId}")
    public DownloadProgress progress(@PathVariable String downloadId) {

        DownloadProgress progress = progressService.getProgress(downloadId);

        if (progress == null) {

            progress = new DownloadProgress();

            progress.setStatus("Download ID Not Found");

        }

        return progress;

    }

}