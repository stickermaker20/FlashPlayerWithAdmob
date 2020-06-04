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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.prog2app.play.Downloaders.FbVideoDownloader;

public class GetVideoUrlActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    LinearLayout linearLayout;
    RadioGroup radioGroup;
    RadioButton btn =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_video_url);
        button=(Button) findViewById(R.id.button);
        editText=(EditText) findViewById(R.id.editText);
        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        radioGroup = findViewById(R.id.radioGroup);
        setTitle("Play Video Downloader");
        internetPermission();
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        final String downloader=getIntent().getStringExtra("Downloader");
        if(downloader.equals("Dailymotion") || downloader.equals("Vimeo")){
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

                    }else if(downloader.equals("Dailymotion")){

                    }else if(downloader.equals("Tiktok")){

                    }else if(downloader.equals("Twitter")){

                    }else if(downloader.equals("Topbuzz")){

                    }else if(downloader.equals("Vimeo")){

                    }else {                    }
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
