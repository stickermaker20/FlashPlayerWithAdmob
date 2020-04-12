package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class ViewVideo extends AppCompatActivity {
    VideoView videoView;
    private View decorView;
    ImageView backArrow,screenLock,previous,next,play,pause,fullScreen,smallScreen,cropScreen,rotateScreen;
    TextView videoTitleText;
    ArrayList<String> videoUri;
    ArrayList<String> videoTitle;
    int position;
    RelativeLayout mainLayout;
    FrameLayout topFrameLayout,bottomFrameLayout;
    boolean framesVisibility=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        videoTitleText=(TextView) findViewById(R.id.videoTitle);
        backArrow=(ImageView) findViewById(R.id.backArrow);
        videoView=(VideoView) findViewById(R.id.videoView);
        screenLock=(ImageView) findViewById(R.id.screenLock);
        previous=(ImageView) findViewById(R.id.previous);
        next=(ImageView) findViewById(R.id.next);
        play=(ImageView)findViewById(R.id.play);
        pause=(ImageView)findViewById(R.id.pause);
        fullScreen=(ImageView) findViewById(R.id.fullScreen);
        smallScreen=(ImageView) findViewById(R.id.smallScreen);
        cropScreen=(ImageView) findViewById(R.id.cropScreen);
        rotateScreen=(ImageView) findViewById(R.id.rotateScreen);
        topFrameLayout =(FrameLayout) findViewById(R.id.topFrameLayout);
        bottomFrameLayout=(FrameLayout) findViewById(R.id.bottomFrameLayout);
        mainLayout=(RelativeLayout) findViewById(R.id.mainLayout);
        videoUri=getIntent().getStringArrayListExtra("VideoUri");
        videoTitle=getIntent().getStringArrayListExtra("VideoTitle");
        position=getIntent().getIntExtra("VideoPosition",0);
        videoTitleText.setText(videoTitle.get(position));
        videoView.setVideoURI(Uri.parse(videoUri.get(position)));
        videoView.start();
        topFrameLayout.setVisibility(View.GONE);
        bottomFrameLayout.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        cropScreen.setVisibility(View.GONE);
        smallScreen.setVisibility(View.GONE);
        rotateScreen.setVisibility(View.GONE);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(framesVisibility){
                    topFrameLayout.setVisibility(View.GONE);
                    bottomFrameLayout.setVisibility(View.GONE);
                    rotateScreen.setVisibility(View.GONE);
                    framesVisibility=false;
                }else{
                    topFrameLayout.setVisibility(View.VISIBLE);
                    bottomFrameLayout.setVisibility(View.VISIBLE);
                    rotateScreen.setVisibility(View.VISIBLE);
                    framesVisibility=true;
                }
            }
        });
        //videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
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
