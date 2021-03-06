package com.prog2app.mp4videoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.prog2app.mp4videoplayer.Downloaders.DailyMotionDownloader;
import com.prog2app.mp4videoplayer.Downloaders.FbVideoDownloader;
import com.prog2app.mp4videoplayer.Downloaders.InstagramVideoDownloader;
import com.prog2app.mp4videoplayer.Downloaders.TiktokVideoDownloader;
import com.prog2app.mp4videoplayer.Downloaders.TopBuzzDownloader;
import com.prog2app.mp4videoplayer.Downloaders.TwitterVideoDownloader;
import com.prog2app.mp4videoplayer.Downloaders.VimeoVideoDownloader;

public class GetVideoUrlActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    LinearLayout radioGroupLayout;
    RadioGroup radioGroup;
    RadioButton btn =  null;
    TextView pointsText,pointOne,pointTwo,noteText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_video_url);
        button=(Button) findViewById(R.id.button);
        editText=(EditText) findViewById(R.id.editText);
        pointOne=(TextView) findViewById(R.id.pointOne);
        pointTwo=(TextView) findViewById(R.id.pointTwo);
        pointsText=(TextView) findViewById(R.id.pointsText);
        noteText=(TextView) findViewById(R.id.noteText);

        radioGroupLayout=(LinearLayout) findViewById(R.id.radioGroupLayout);
        radioGroup = findViewById(R.id.radioGroup);
        setTitle("Play Video Downloader");
        internetPermission();
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        final String downloader=getIntent().getStringExtra("Downloader");
        pointsText.setText("We can help you to download "+downloader+" video follow these steps :");
        pointOne.setText("1. Open your "+downloader+" app, open and watch wanted video for few seconds then copy video's link or path.");
        pointTwo.setText("2. Come back here, paste the video path or link in the below box and click 'DOWNLOAD VIDEO' button.");
        noteText.setText("If video is not downloading watch it for few seconds then copy link.\n\nYour video will automatically download in background. Please wait after clicking 'DOWNLOAD BUTTON'.");
        if(downloader.equals("Dailymotion")){
            radioGroupLayout.setVisibility(View.VISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isConnectingToInternet()) {
                    String videoUrl=editText.getText().toString();
                    if(!videoUrl.equals("") && !videoUrl.equals(null)){
                        try{
                    if(downloader.equals("Facebook")){
                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(GetVideoUrlActivity.this,videoUrl);
                        fbVideoDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Instagram")){
                        InstagramVideoDownloader instagramVideoDownloader = new InstagramVideoDownloader(GetVideoUrlActivity.this,videoUrl);
                        instagramVideoDownloader.DownloadVideo();
                        editText.setText("");

                    }else if(downloader.equals("Dailymotion")){
                        DailyMotionDownloader dailyMotionDownloader = new DailyMotionDownloader(GetVideoUrlActivity.this,getVideoQuality(),videoUrl,12);
                        dailyMotionDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Tiktok")){
                        TiktokVideoDownloader tiktokVideoDownloader = new TiktokVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        tiktokVideoDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Twitter")){
                        TwitterVideoDownloader twitterVideoDownloader = new TwitterVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        twitterVideoDownloader.DownloadVideo();
                        editText.setText("");


                    }else if(downloader.equals("Topbuzz")){
                        TopBuzzDownloader topBuzzDownloader = new TopBuzzDownloader(GetVideoUrlActivity.this, videoUrl, 12);
                        topBuzzDownloader.DownloadVideo();
                        editText.setText("");

                    }else if(downloader.equals("Vimeo")){
                        VimeoVideoDownloader vimeoVideoDownloader = new VimeoVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        vimeoVideoDownloader.DownloadVideo();
                        editText.setText("");
                    }
               }catch (Exception e){
                            Snackbar.make(findViewById(R.id.mainLayout),""+e.getMessage(),Snackbar.LENGTH_LONG).show();
                        }
                    }else{
                        Snackbar.make(findViewById(R.id.mainLayout),"Sorry, first paste video URL",Snackbar.LENGTH_LONG).show();

                    }
               }
               else {
                   Snackbar.make(findViewById(R.id.mainLayout),"Sorry, no internet",Snackbar.LENGTH_LONG).show();
               }
           }
       });



    }

    private void internetPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                recreate();
                return;
            }
        }
    }
    //Check if internet is present or not
    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private String getVideoQuality()
    {
        if(radioGroup.getCheckedRadioButtonId() != -1)
        {
            btn = findViewById(radioGroup.getCheckedRadioButtonId());
        }
        return btn.getText().toString();
    }
}
