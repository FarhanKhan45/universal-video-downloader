package com.jarvis.videodownloader.controller;

import com.jarvis.videodownloader.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PrepareController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/prepare")
    public Map<String, String> prepare(
            @RequestParam String url,
            @RequestParam String formatId) {

        String downloadId =
                downloadService.prepareDownload(
                        url,
                        formatId);

        Map<String, String> response =
                new HashMap<>();

        response.put("downloadId", downloadId);

        return response;

    }

}