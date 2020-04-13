package com.example.videoaudioplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Rational;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.w3c.dom.Text;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewVideo extends AppCompatActivity{
    private final PictureInPictureParams.Builder pictureInPictureParamsBuilder =
            new PictureInPictureParams.Builder();
    VideoView videoView;
    private View decorView;
    ImageView backArrow,screenLock,previous,next,play,pause,fullScreen,smallScreen,cropScreen,rotateScreen,screenUnlock,popupScreen;
    TextView videoTitleText,currentTime,leftTime,screenSizeText;
    ArrayList<String> videoUri;
    ArrayList<String> videoTitle;
    int position;
    RelativeLayout mainLayout;
    FrameLayout topFrameLayout,bottomFrameLayout;
    boolean framesVisibility=false;
    boolean screenLandscape=true;
    boolean lockScreen=false;
    SeekBar seekbar;
    private Handler updateHandler = new Handler();
    int dur = 0;
    int current = 0;
    String videoCurrentTime,videoLeftTime;
    int videoPauseTime;
    boolean videoPlayed;

    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            dur = videoView.getDuration();
            long currentPosition = videoView.getCurrentPosition();
            seekbar.setProgress((int) currentPosition);
            updateHandler.postDelayed(this, 100);
            current = videoView.getCurrentPosition();
            int currentmns = (int) ((current % 3600000.0d) / 60000.0d);
            int currentscs = (int) (((current % 3600000.0d) % 60000.0d) / 1000.0d);
            if (((int) (current / 3600000.0d)) > 0) {
                videoCurrentTime = String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (current / 3600000.0d)), Integer.valueOf(currentmns), Integer.valueOf(currentscs)});
            } else {
                videoCurrentTime = String.format("%02d:%02d", new Object[]{Integer.valueOf(currentmns), Integer.valueOf(currentscs)});
            }
            int leftmns = (dur % 3600000) / 60000;
            int leftscs = ((dur % 3600000) % 60000) / 1000;
            if (dur / 3600000 > 0) {
                videoLeftTime = String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(dur / 3600000), Integer.valueOf(leftmns), Integer.valueOf(leftscs)});
            } else {
                videoLeftTime = String.format("%02d:%02d", new Object[]{Integer.valueOf(leftmns), Integer.valueOf(leftscs)});
            }
            currentTime.setText(videoCurrentTime);
            leftTime.setText(videoLeftTime);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        videoPauseTime=videoView.getCurrentPosition();
        if(videoView.isPlaying()){
            videoView.pause();
            videoPlayed=true;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (videoPlayed) {
            videoView.seekTo(videoPauseTime);
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        }else{
            videoView.seekTo(videoPauseTime);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);
        videoTitleText=(TextView) findViewById(R.id.videoTitle);
        backArrow=(ImageView) findViewById(R.id.backArrow);
        videoView=(VideoView) findViewById(R.id.videoView);
        screenLock=(ImageView) findViewById(R.id.screenLock);
        screenUnlock=(ImageView) findViewById(R.id.screenUnlock);
        previous=(ImageView) findViewById(R.id.previous);
        next=(ImageView) findViewById(R.id.next);
        play=(ImageView)findViewById(R.id.play);
        pause=(ImageView)findViewById(R.id.pause);
        popupScreen=(ImageView)findViewById(R.id.popupScreen);
        screenSizeText=(TextView) findViewById(R.id.screenSizeText);
        currentTime=(TextView) findViewById(R.id.startTime);
        leftTime=(TextView) findViewById(R.id.endTime);
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
        screenUnlock.setVisibility(View.GONE);

        seekbar = (SeekBar) findViewById(R.id.video_seekbar);

        //time and seekbar code starts here
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekbar.setProgress(0);
                seekbar.setMax(videoView.getDuration());
                updateHandler.postDelayed(updateVideoTime, 100);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    // Mean that the seekbar value is changed by user
                    videoView.seekTo(progress); // Verify this
                } else {
                    // Ignore becuase is due to seekBar programmatically change

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //new MyAsync().execute();

//        currentTime.setText(videoView.getCurrentPosition());
  //      leftTime.setText(videoView.getDuration());


        //my code
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!lockScreen){
                if(framesVisibility){
                    topFrameLayout.setVisibility(View.GONE);
                    bottomFrameLayout.setVisibility(View.GONE);
                    rotateScreen.setVisibility(View.GONE);
                    framesVisibility=false;

                }else{
                    topFrameLayout.setVisibility(View.VISIBLE);
                    bottomFrameLayout.setVisibility(View.VISIBLE);
                    rotateScreen.setVisibility(View.VISIBLE);
                    if(videoView.isPlaying()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!framesVisibility) {
                                    topFrameLayout.setVisibility(View.GONE);
                                    bottomFrameLayout.setVisibility(View.GONE);
                                    rotateScreen.setVisibility(View.GONE);
                                    framesVisibility=false;
                                }
                            }
                        }, 4000);
                    }else{
                        framesVisibility=true;
                    }
                }
            }else{
                screenUnlock.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            screenUnlock.setVisibility(View.GONE);
                        }
                    }, 4000);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=position-1;
                if(position<0){
                    position=videoUri.size()-1;
                }
                videoTitleText.setText(videoTitle.get(position));
                videoView.setVideoURI(Uri.parse(videoUri.get(position)));

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=position+1;
                if(position>(videoUri.size()-1)){
                    position=0;
                }
                videoTitleText.setText(videoTitle.get(position));
                videoView.setVideoURI(Uri.parse(videoUri.get(position)));

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                topFrameLayout.setVisibility(View.VISIBLE);
                bottomFrameLayout.setVisibility(View.VISIBLE);
                rotateScreen.setVisibility(View.VISIBLE);
                framesVisibility=true;


            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                if(videoView.isPlaying()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            topFrameLayout.setVisibility(View.GONE);
                            bottomFrameLayout.setVisibility(View.GONE);
                            rotateScreen.setVisibility(View.GONE);
                            framesVisibility=false;
                        }
                    }, 4000);
                }
            }
        });
        rotateScreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View v) {
                if(!screenLandscape) {
                    setRequestedOrientation(0);
                    screenLandscape=true;
                    videoView.start();
                }else{
                    screenLandscape=false;
                    setRequestedOrientation(1);
                }
            }
        });
        screenLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockScreen=true;
                topFrameLayout.setVisibility(View.GONE);
                bottomFrameLayout.setVisibility(View.GONE);
                rotateScreen.setVisibility(View.GONE);
                screenUnlock.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenUnlock.setVisibility(View.GONE);
                    }
                }, 4000);

            }
        });
        screenUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockScreen=false;
                topFrameLayout.setVisibility(View.VISIBLE);
                bottomFrameLayout.setVisibility(View.VISIBLE);
                rotateScreen.setVisibility(View.VISIBLE);
                screenUnlock.setVisibility(View.GONE);
            }
        });
        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreen.setVisibility(View.GONE);
                cropScreen.setVisibility(View.VISIBLE);
                DisplayMetrics metrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                params.width = metrics.widthPixels;
                params.height = metrics.heightPixels;
                params.leftMargin = 0;
                params.rightMargin = 0;
                params.topMargin = 0;
                params.bottomMargin = 0;
                ViewVideo.this.videoView.setLayoutParams(params);
                screenSizeText.setText("FIT TO SCREEN");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenSizeText.setText("");
                    }
                }, 4000);

            }
        });
        cropScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropScreen.setVisibility(View.GONE);
                smallScreen.setVisibility(View.VISIBLE);
                DisplayMetrics metrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                params.width = (int) (400.0f * metrics.density);
                params.height = (int) (BitmapDescriptorFactory.HUE_MAGENTA * metrics.density);
                params.leftMargin = 150;
                params.rightMargin = 150;
                params.topMargin = 150;
                params.bottomMargin = 150;
                ViewVideo.this.videoView.setLayoutParams(params);

                screenSizeText.setText("CROP");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenSizeText.setText("");
                    }
                }, 4000);

            }
        });
        smallScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallScreen.setVisibility(View.GONE);
                fullScreen.setVisibility(View.VISIBLE);
                DisplayMetrics metrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                params.width = (int) (1000.0f * metrics.density);
                params.leftMargin = 150;
                params.rightMargin = 150;
                params.topMargin = 0;
                params.bottomMargin = 0;
                ViewVideo.this.videoView.setLayoutParams(params);
                screenSizeText.setText("100%");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        screenSizeText.setText("");
                    }
                }, 4000);

            }
        });
        popupScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rational aspectRatio = new Rational(videoView.getWidth(), videoView.getHeight());
                pictureInPictureParamsBuilder.setAspectRatio(aspectRatio).build();
                enterPictureInPictureMode(pictureInPictureParamsBuilder.build());
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

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
