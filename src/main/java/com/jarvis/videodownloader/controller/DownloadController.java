package com.jarvis.videodownloader.controller;

import com.jarvis.videodownloader.model.DownloadProgress;
import com.jarvis.videodownloader.service.DownloadService;
import com.jarvis.videodownloader.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private ProgressService progressService;

    @GetMapping("/download/{downloadId}")
    public ResponseEntity<Resource> download(
            @PathVariable String downloadId) {

        DownloadProgress progress =
                progressService.getProgress(downloadId);

        if (progress == null || !progress.isReady()) {

            return ResponseEntity.notFound().build();

        }

        File file =
                downloadService.getDownloadedFile(downloadId);

        if (file == null || !file.exists()) {

            return ResponseEntity.notFound().build();

        }

        Resource resource = new FileSystemResource(file);

        String safeFileName = file.getName()
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + safeFileName + "\"")
                .body(resource);

    }

}