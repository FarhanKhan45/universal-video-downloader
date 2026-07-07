package com.jarvis.videodownloader.model;

import java.util.ArrayList;
import java.util.List;

public class VideoInfo {

    private String title;
    private String thumbnail;
    private int duration;
    private String uploader;
    private String url;

    // New
    private List<VideoFormat> formats = new ArrayList<>();

    public VideoInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<VideoFormat> getFormats() {
        return formats;
    }

    public void setFormats(List<VideoFormat> formats) {
        this.formats = formats;
    }
}