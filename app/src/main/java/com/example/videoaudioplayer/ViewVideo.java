package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class ViewVideo extends AppCompatActivity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        videoView=(VideoView) findViewById(R.id.videoView);
        String videoUri=getIntent().getStringExtra("VideoUri");
        setTitle(videoUri);
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.start();
    }
}
