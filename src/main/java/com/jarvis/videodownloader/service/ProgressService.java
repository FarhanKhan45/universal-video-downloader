package com.jarvis.videodownloader.service;

import com.jarvis.videodownloader.model.DownloadProgress;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProgressService {

    private final Map<String, DownloadProgress> progressMap =
            new ConcurrentHashMap<>();

    public void create(String downloadId) {

        DownloadProgress progress = new DownloadProgress();

        progress.setPercentage(0);
        progress.setSpeed("");
        progress.setEta("");
        progress.setStatus("Preparing");
        progress.setReady(false);
        progress.setFilePath("");

        progressMap.put(downloadId, progress);

    }

    public void updateProgress(String downloadId,
                               int percentage,
                               String speed,
                               String eta,
                               String status) {

        DownloadProgress progress = progressMap.get(downloadId);

        if (progress == null) {

            progress = new DownloadProgress();

            progressMap.put(downloadId, progress);

        }

        progress.setPercentage(percentage);
        progress.setSpeed(speed);
        progress.setEta(eta);
        progress.setStatus(status);

    }

    public void setReady(String downloadId,
                         String filePath) {

        DownloadProgress progress = progressMap.get(downloadId);

        if (progress != null) {

            progress.setReady(true);
            progress.setFilePath(filePath);
            progress.setPercentage(100);
            progress.setStatus("Ready");

        }

    }

    public DownloadProgress getProgress(String downloadId) {

        return progressMap.get(downloadId);

    }

    public void remove(String downloadId) {

        progressMap.remove(downloadId);

    }

}