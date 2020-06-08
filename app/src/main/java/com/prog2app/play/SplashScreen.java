package com.prog2app.play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {
    private ImageView logo;
    boolean darkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences darkModeSP = getSharedPreferences("Log", MODE_PRIVATE);
        darkMode = darkModeSP.getBoolean("AppDarkMode", false);
        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_splash_screen);
        logo=(ImageView)findViewById(R.id.logo);




        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimaation);
        logo.startAnimation(myanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this,AudioVideoFolders.class);
                startActivity(i);
                finish();
            }
        },2000);
    }

}
