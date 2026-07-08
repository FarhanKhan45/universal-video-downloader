# 🎬 Universal Video Downloader

A production-ready Universal Video Downloader built using **Java 21**, **Spring Boot**, and **yt-dlp**.

The application allows users to fetch video information and download supported videos through a clean web interface.

🌐 Live Demo

https://videodownloader.mooo.com

---

# Features

- Download videos from supported platforms
- Fetch video title, thumbnail, duration and uploader
- Multiple quality selection
- Download progress tracking
- Responsive UI
- Background downloading
- HTTPS enabled
- Production deployment on AWS EC2
- Automated CI/CD using GitHub Actions

---

# Tech Stack

## Backend

- Java 21
- Spring Boot 3.5
- Spring MVC
- Maven
- REST APIs

## Frontend

- HTML5
- CSS3
- JavaScript
- Thymeleaf

## Video Processing

- yt-dlp
- FFmpeg

## DevOps

- AWS EC2
- Amazon Linux 2023
- Nginx Reverse Proxy
- Let's Encrypt SSL
- GitHub Actions CI/CD
- Git
- GitHub

---

# Architecture

```
Browser
      │
 HTTPS
      │
Nginx Reverse Proxy
      │
Spring Boot
      │
ProcessBuilder
      │
yt-dlp
      │
FFmpeg
```

---

# Current Features

✅ Video Information

- Title
- Thumbnail
- Duration
- Uploader

✅ Download

- Multiple quality selection
- Progress tracking
- ETA
- Download speed
- Automatic merging using FFmpeg

---

# Deployment

The application is deployed on:

- AWS EC2
- Nginx
- HTTPS
- GitHub Actions CI/CD

Every push to the **main** branch automatically deploys the latest version to the production server.

---

# Future Roadmap

- Spring Boot Microservices
- API Gateway
- Video Service
- Download Service
- RabbitMQ
- Redis
- Docker
- Kubernetes
- AWS S3
- gRPC
- Prometheus
- Grafana

---

# Project Status

This project is under active development.

The current production version focuses on building a scalable backend architecture while continuously improving support for external video platforms.

---

# Author

**Farhan Khan**

Designed & Developed by Farhan Khan
