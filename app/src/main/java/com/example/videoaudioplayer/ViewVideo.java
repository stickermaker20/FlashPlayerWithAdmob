package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class ViewVideo extends AppCompatActivity {
    VideoView videoView;
    private View decorView;
    ImageView backArrow;
    TextView videoTitleText;
    ArrayList<String> videoUri;
    ArrayList<String> videoTitle;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        videoTitleText=(TextView) findViewById(R.id.videoTitle);
        backArrow=(ImageView) findViewById(R.id.backArrow);
        videoView=(VideoView) findViewById(R.id.videoView);
        videoUri=getIntent().getStringArrayListExtra("VideoUri");
        videoTitle=getIntent().getStringArrayListExtra("VideoTitle");
        position=getIntent().getIntExtra("VideoPosition",0);
        videoTitleText.setText(videoTitle.get(position));
        videoView.setVideoURI(Uri.parse(videoUri.get(position)));
        videoView.start();
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //status bar code
        decorView=getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                decorView.setSystemUiVisibility(hideSystemBars());
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }
    private  int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
    }
}
