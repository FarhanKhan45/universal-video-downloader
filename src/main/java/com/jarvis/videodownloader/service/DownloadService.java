package com.jarvis.videodownloader.service;

import com.jarvis.videodownloader.model.DownloadProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.UUID;

@Service
public class DownloadService {

    @Autowired
    private ProgressService progressService;

    public String prepareDownload(String url,
                                  String formatId) {

        String downloadId =
                UUID.randomUUID().toString();

        progressService.create(downloadId);

        new Thread(() -> {

            try {

                download(
                        downloadId,
                        url,
                        formatId
                );

            }

            catch (Exception e) {

                progressService.updateProgress(
                        downloadId,
                        0,
                        "",
                        "",
                        "Failed"
                );

            }

        }).start();

        return downloadId;

    }    private void download(String downloadId,
            String url,
            String formatId) throws Exception {

String folderName = "downloads/" + downloadId;

File dir = new File(folderName);

dir.mkdirs();

String output =
folderName + "/%(id)s.%(ext)s";
ProcessBuilder pb;

if ("bestaudio".equals(formatId)) {

	pb = new ProcessBuilder(
	        "yt-dlp",
	        "-x",
	        "--audio-format",
	        "mp3",
	        "--restrict-filenames",
	        "--windows-filenames",
	        "-o",
	        output,
	        url
	);
}

else {
	pb = new ProcessBuilder(
	        "yt-dlp",
	        "-f",
	        formatId,
	        "--merge-output-format",
	        "mp4",
	        "--restrict-filenames",
	        "--windows-filenames",
	        "-o",
	        output,
	        url
	);

}

pb.redirectErrorStream(true);

Process process = pb.start();

BufferedReader reader =
  new BufferedReader(
          new InputStreamReader(
                  process.getInputStream()));

String line;

while ((line = reader.readLine()) != null) {

System.out.println(line);

if (line.contains("[download]")) {

  try {

      String txt =
              line.replace("[download]", "")
                      .trim();

      String[] parts =
              txt.split("\\s+");

      int percentage = 0;

      if (parts.length > 0 &&
              parts[0].contains("%")) {

          percentage =
                  (int) Double.parseDouble(
                          parts[0]
                                  .replace("%", ""));

      }

      String speed = "";

      String eta = "";
      for (int i = 0; i < parts.length; i++) {

          if (parts[i].equalsIgnoreCase("at")
                  && i + 1 < parts.length) {

              speed = parts[i + 1];

          }

          if (parts[i].equalsIgnoreCase("ETA")
                  && i + 1 < parts.length) {

              eta = parts[i + 1];

          }

      }

      progressService.updateProgress(

              downloadId,

              percentage,

              speed,

              eta,

              "Downloading"

      );

  }

  catch (Exception ignored) {

  }

}

}

int exitCode = process.waitFor();

if (exitCode != 0) {

progressService.updateProgress(

      downloadId,

      0,

      "",

      "",

      "Failed"

);

return;

}

File[] files = dir.listFiles();

if (files == null || files.length == 0) {

progressService.updateProgress(

      downloadId,

      0,

      "",

      "",

      "File not found"

);

return;

}

progressService.setReady(

  downloadId,

  files[0].getAbsolutePath()

);

}    public File getDownloadedFile(String downloadId) {

    DownloadProgress progress =
            progressService.getProgress(downloadId);

    if (progress == null) {

        return null;

    }

    if (!progress.isReady()) {

        return null;

    }

    return new File(progress.getFilePath());

}

}