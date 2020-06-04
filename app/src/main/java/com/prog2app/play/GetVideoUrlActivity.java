package com.prog2app.play;

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
import com.prog2app.play.Downloaders.DailyMotionDownloader;
import com.prog2app.play.Downloaders.FbVideoDownloader;
import com.prog2app.play.Downloaders.InstagramVideoDownloader;
import com.prog2app.play.Downloaders.TiktokVideoDownloader;
import com.prog2app.play.Downloaders.TopBuzzDownloader;
import com.prog2app.play.Downloaders.TwitterVideoDownloader;
import com.prog2app.play.Downloaders.VimeoVideoDownloader;

public class GetVideoUrlActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    LinearLayout linearLayout;
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

        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        radioGroup = findViewById(R.id.radioGroup);
        setTitle("Play Video Downloader");
        internetPermission();
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        final String downloader=getIntent().getStringExtra("Downloader");
        pointsText.setText("We can help you to download "+downloader+" video follow these steps :");
        pointOne.setText("1. Open your "+downloader+" app and copy wanted video's link or path.");
        pointTwo.setText("2. Come back here, paste the video path or link in the below box and click 'DOWNLOAD VIDEO' button.");
        noteText.setText("Your video will automatically download in background.");
        if(downloader.equals("Dailymotion")){
            linearLayout.setVisibility(View.VISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (isConnectingToInternet()) {
                    String videoUrl=editText.getText().toString();
                    if(!videoUrl.equals("") && !videoUrl.equals(null)){
                    if(downloader.equals("Facebook")){
                        FbVideoDownloader fbVideoDownloader = new FbVideoDownloader(GetVideoUrlActivity.this,videoUrl);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        fbVideoDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Instagram")){
                        InstagramVideoDownloader instagramVideoDownloader = new InstagramVideoDownloader(GetVideoUrlActivity.this,videoUrl);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        instagramVideoDownloader.DownloadVideo();
                        editText.setText("");

                    }else if(downloader.equals("Dailymotion")){
                        DailyMotionDownloader dailyMotionDownloader = new DailyMotionDownloader(GetVideoUrlActivity.this,getVideoQuality(),videoUrl,12);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        dailyMotionDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Tiktok")){
                        TiktokVideoDownloader tiktokVideoDownloader = new TiktokVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        tiktokVideoDownloader.DownloadVideo();
                        editText.setText("");
                    }else if(downloader.equals("Twitter")){
                        TwitterVideoDownloader twitterVideoDownloader = new TwitterVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        twitterVideoDownloader.DownloadVideo();
                        editText.setText("");


                    }else if(downloader.equals("Topbuzz")){
                        TopBuzzDownloader topBuzzDownloader = new TopBuzzDownloader(GetVideoUrlActivity.this, videoUrl, 12);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        topBuzzDownloader.DownloadVideo();
                        editText.setText("");

                    }else if(downloader.equals("Vimeo")){
                        VimeoVideoDownloader vimeoVideoDownloader = new VimeoVideoDownloader(GetVideoUrlActivity.this, videoUrl);
                        Snackbar.make(findViewById(R.id.mainLayout),"Video is downloading in background",Snackbar.LENGTH_LONG).show();
                        vimeoVideoDownloader.DownloadVideo();
                        editText.setText("");
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
