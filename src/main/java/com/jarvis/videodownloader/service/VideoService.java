package com.jarvis.videodownloader.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.videodownloader.model.VideoFormat;
import com.jarvis.videodownloader.model.VideoInfo;

@Service
public class VideoService {

    public VideoInfo fetchVideoInfo(String url) throws Exception {

        ProcessBuilder processBuilder = new ProcessBuilder(
                "yt-dlp",
                "-J",
                url);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        StringBuilder json = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            json.append(line);

        }

        process.waitFor();

        ObjectMapper mapper = new ObjectMapper();

        JsonNode node = mapper.readTree(json.toString());

        VideoInfo video = new VideoInfo();

        video.setTitle(node.path("title").asText());

        video.setThumbnail(node.path("thumbnail").asText());

        video.setDuration(node.path("duration").asInt());

        video.setUploader(node.path("uploader").asText());

        video.setUrl(url);

        List<VideoFormat> formats = new ArrayList<>();

        Set<String> addedQualities = new HashSet<>();

        JsonNode formatArray = node.path("formats");

        if (formatArray.isArray()) {

            for (JsonNode f : formatArray) {

                if (!f.has("format_id"))
                    continue;

                if (!f.has("ext"))
                    continue;

                if (!"mp4".equals(f.get("ext").asText()))
                    continue;

                if (!f.has("height"))
                    continue;

                int height = f.get("height").asInt();

                if (height <= 0)
                    continue;

                String quality = height + "p";

                if (addedQualities.contains(quality))
                    continue;

                addedQualities.add(quality);

                VideoFormat vf = new VideoFormat();

                String formatId = f.get("format_id").asText();

                String acodec = f.has("acodec")
                        ? f.get("acodec").asText()
                        : "none";

                if ("none".equals(acodec)) {

                    vf.setFormatId(formatId + "+bestaudio");

                    vf.setAudio(false);

                } else {

                    vf.setFormatId(formatId);

                    vf.setAudio(true);

                }

                vf.setVideo(true);

                vf.setExtension("MP4");

                if (height >= 1080) {

                    vf.setQuality("🎬 " + quality + " Full HD");

                } else if (height >= 720) {

                    vf.setQuality("🎬 " + quality + " HD");

                } else {

                    vf.setQuality("🎬 " + quality);

                }

                if (f.has("filesize") && !f.get("filesize").isNull()) {

                    double mb = f.get("filesize").asDouble()
                            / 1024
                            / 1024;

                    vf.setFileSize(String.format("%.1f MB", mb));

                } else {

                    vf.setFileSize("Unknown");

                }

                formats.add(vf);

            }

        }

        // MP3 Audio Option

        VideoFormat mp3 = new VideoFormat();

        mp3.setFormatId("bestaudio");

        mp3.setQuality("🎵 MP3 Audio");

        mp3.setExtension("MP3");

        mp3.setFileSize("-");

        mp3.setAudio(true);

        mp3.setVideo(false);

        formats.add(mp3);

        video.setFormats(formats);

        return video;

    }

}