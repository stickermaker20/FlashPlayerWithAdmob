package com.prog2app.mp4videoplayer.Interfaces;

public interface VideoDownloader {

    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
