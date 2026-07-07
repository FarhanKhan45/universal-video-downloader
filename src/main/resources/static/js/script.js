document.addEventListener("DOMContentLoaded", () => {

    const fetchForm = document.getElementById("fetchForm");
    const loading = document.getElementById("loading");
    const videoContainer = document.getElementById("videoContainer");
    const errorDiv = document.getElementById("error");
    const fetchButton = document.getElementById("fetchButton");

    fetchForm.addEventListener("submit", async (e) => {

        e.preventDefault();

        loading.style.display = "block";
        videoContainer.innerHTML = "";
        errorDiv.innerHTML = "";

        fetchButton.disabled = true;
        fetchButton.innerHTML = "Fetching...";

        const formData = new FormData(fetchForm);

        try {

            const response = await fetch("/fetch", {
                method: "POST",
                body: formData
            });

            if (!response.ok) {
                throw new Error("Unable to fetch video.");
            }

            const video = await response.json();

            loading.style.display = "none";

            fetchButton.disabled = false;
            fetchButton.innerHTML = "Fetch";

            let qualityOptions = "";

            video.formats.forEach(format => {

                qualityOptions += `
                    <option value="${format.formatId}">
                        ${format.quality} (${format.fileSize})
                    </option>
                `;

            });

            videoContainer.innerHTML = `

            <div class="video-card">

                <div class="video-left">

                    <img src="${video.thumbnail}" alt="Thumbnail">

                </div>

                <div class="video-right">

                    <h2>${video.title}</h2>

                    <div class="info">

                        <p>👤 <strong>Uploader:</strong> ${video.uploader}</p>

                        <p>⏱ <strong>Duration:</strong> ${video.duration} Seconds</p>

                    </div>

                    <div class="quality-box">

                        <label><strong>Select Quality</strong></label>

                        <select id="quality">

                            ${qualityOptions}

                        </select>

                    </div>

                    <button
                        id="downloadBtn"
                        class="download-btn">

                        ⬇ Download Video

                    </button>

                    <div
                        id="progressContainer"
                        style="display:none;">

                        <h3>Preparing Download...</h3>

                        <div class="progress">

                            <div id="progressBar"></div>

                        </div>

                        <h2 id="percentage">0%</h2>

                        <p>⚡ Speed :
                            <span id="speed">-</span>
                        </p>

                        <p>⏳ ETA :
                            <span id="eta">-</span>
                        </p>

                        <p>📌 Status :
                            <span id="status">Waiting...</span>
                        </p>

                    </div>

                </div>

            </div>

            `;

            attachDownloadListener(video.url);

        }

        catch (err) {

            loading.style.display = "none";

            fetchButton.disabled = false;

            fetchButton.innerHTML = "Fetch";

            errorDiv.innerHTML = err.message;

        }

    });

});	function attachDownloadListener(videoUrl) {

	    const downloadBtn =
	            document.getElementById("downloadBtn");

	    if (!downloadBtn) {

	        return;

	    }

	    downloadBtn.addEventListener("click", async () => {

	        downloadBtn.disabled = true;

	        downloadBtn.innerHTML =
	                "⏳ Preparing Download...";

	        document.getElementById(
	                "progressContainer"
	        ).style.display = "block";

	        const quality =
	                document.getElementById(
	                        "quality"
	                ).value;

	        const formData =
	                new FormData();

	        formData.append(
	                "url",
	                videoUrl
	        );

	        formData.append(
	                "formatId",
	                quality
	        );

	        try {

	            const response =
	                    await fetch(
	                            "/prepare",
	                            {
	                                method: "POST",
	                                body: formData
	                            }
	                    );

	            if (!response.ok) {

	                throw new Error(
	                        "Unable to start download."
	                );

	            }

	            const data =
	                    await response.json();

	            startProgress(
	                    data.downloadId,
	                    downloadBtn
	            );

	        }

	        catch (e) {

	            alert(e.message);

	            downloadBtn.disabled = false;

	            downloadBtn.innerHTML =
	                    "⬇ Download Video";

	        }

	    });

	}	function startProgress(downloadId, downloadBtn) {

	    const progressBar = document.getElementById("progressBar");
	    const percentage = document.getElementById("percentage");
	    const speed = document.getElementById("speed");
	    const eta = document.getElementById("eta");
	    const status = document.getElementById("status");

	    const timer = setInterval(async () => {

	        try {

	            const response = await fetch("/progress/" + downloadId);

	            if (!response.ok) {
	                return;
	            }

	            const progress = await response.json();

	            percentage.innerHTML = progress.percentage + "%";

	            progressBar.style.width = progress.percentage + "%";

	            speed.innerHTML = progress.speed || "-";

	            eta.innerHTML = progress.eta || "-";

	            status.innerHTML = progress.status;

	            if (progress.ready) {

	                clearInterval(timer);

	                progressBar.style.width = "100%";

	                percentage.innerHTML = "100%";

	                status.innerHTML = "✅ Download Ready";

	                downloadBtn.innerHTML = "⬇ Downloading...";

	                setTimeout(() => {

	                    const link = document.createElement("a");

	                    link.href = "/download/" + downloadId;

	                    link.setAttribute("download", "");

	                    document.body.appendChild(link);

	                    link.click();

	                    document.body.removeChild(link);

	                    downloadBtn.disabled = false;

	                    downloadBtn.innerHTML = "⬇ Download Video";

	                }, 500);

	            }

	        } catch (e) {

	            clearInterval(timer);

	            downloadBtn.disabled = false;

	            downloadBtn.innerHTML = "⬇ Download Video";

	            alert("Download failed.");

	        }

	    }, 1000);

	}	// ===============================
	// Utility Functions
	// ===============================

	function resetProgress() {

	    const progressContainer =
	            document.getElementById("progressContainer");

	    if (progressContainer) {

	        progressContainer.style.display = "none";

	    }

	}

	function setButtonLoading(button, text) {

	    if (!button) {

	        return;

	    }

	    button.disabled = true;

	    button.innerHTML = text;

	}

	function resetButton(button) {

	    if (!button) {

	        return;

	    }

	    button.disabled = false;

	    button.innerHTML = "⬇ Download Video";

	}

	window.addEventListener("beforeunload", () => {

	    resetProgress();

	});