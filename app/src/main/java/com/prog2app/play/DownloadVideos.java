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
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    EditText editText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_videos);
        editText=(EditText) findViewById(R.id.editText);
        button=(Button) findViewById(R.id.button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                recreate();
                return;
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText.getText().toString()
              String link="https://video-amt2-1.xx.fbcdn.net/v/t42.1790-2/101106642_3053635948036566_4206688890385334272_n.mp4?_nc_cat=107&_nc_sid=5aebc0&efg=eyJ2ZW5jb2RlX3RhZyI6ImRhc2hfdjRhdWRpb2ZvcnNvdXJjZXBpcGVsaW5lXzEyOF9jcmZfMjNfbWFpbl8zLjBfZnJhZ18yX2F1ZGlvIn0%3D&_nc_ohc=XWLxZc-6keUAX8i8kdC&_nc_ht=video-amt2-1.xx&oh=342210d86f2bba4a314755991efea698&oe=5ED8F648";
                if (isConnectingToInternet())

                    new DownloadTask(getApplicationContext(), button, link);
                else
                    Toast.makeText(getApplication(), "Oops!! There is no internet connection. Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();

            }
        });

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
}
