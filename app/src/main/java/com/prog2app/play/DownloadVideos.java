package com.prog2app.play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class DownloadVideos extends AppCompatActivity {

ImageView facebookButton,instagramButton,twitterButton,dailymotionButton,tiktokButton,vimeoButton,topbuzzButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_videos);
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
        setTitle("Download Videos");
        facebookButton=(ImageView) findViewById(R.id.facebookButton);
        instagramButton=(ImageView) findViewById(R.id.instagramButton);
        twitterButton=(ImageView) findViewById(R.id.twitterButton);
        dailymotionButton=(ImageView) findViewById(R.id.dailymotionButton);
        tiktokButton=(ImageView) findViewById(R.id.tiktokButton);
        vimeoButton=(ImageView) findViewById(R.id.vimeoButton);
        topbuzzButton=(ImageView) findViewById(R.id.topbuzzButton);
        final Intent intent=new Intent(this,GetVideoUrlActivity.class);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Facebook");
                startActivity(intent);
            }
        });
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Instagram");
                startActivity(intent);
            }
        });
        dailymotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Dailymotion");
                startActivity(intent);
            }
        });
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Twitter");
                startActivity(intent);
            }
        });
        topbuzzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Topbuzz");
                startActivity(intent);
            }
        });
        tiktokButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Tiktok");
                startActivity(intent);
            }
        });
        vimeoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Downloader","Vimeo");
                startActivity(intent);
            }
        });

    }
}
