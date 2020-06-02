package com.prog2app.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AudioVideoFolders extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private boolean linearLayout;
    private Menu menu;
    AudioVideoFoldersFragment audioVideoFoldersFragment=new AudioVideoFoldersFragment();
    protected BottomNavigationView navigationView;
    AudioVideoFoldersFragment selectedFragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_folders);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                recreate();
                return;
            }
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        Bundle bundle = new Bundle();
        SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
        linearLayout = prefs.getBoolean("foldersLinearLayout", true);
        bundle.putString("ListType", "video");
        audioVideoFoldersFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,audioVideoFoldersFragment).commit();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        this.menu=menu;
        changeLayoutIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent viewIntent;
        switch (item.getItemId()){

            case R.id.action_more:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/developer?id=prog2app"));
                startActivity(viewIntent);
                break;
            case R.id.action_rate:
                viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://play.google.com/store/apps/details?id=com.prog2app.play&hl=en"));
                startActivity(viewIntent);

                break;
            case R.id.folderLayout:
                linearLayout=!linearLayout;
//                checkLayout();
                audioVideoFoldersFragment.checkLayout(linearLayout);
                if(selectedFragment!=null){
                    selectedFragment.checkLayout(linearLayout);
                }
                changeLayoutIcon();
                SharedPreferences.Editor editor = getSharedPreferences("Log", MODE_PRIVATE).edit();

                if (linearLayout) {
                    editor.putBoolean("foldersLinearLayout", linearLayout);
                } else {
                    editor.putBoolean("foldersLinearLayout", linearLayout);
                }
                editor.commit();
                break;

        }
        return true;
    }

    private void changeLayoutIcon() {
        if(!linearLayout){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_linear_black_24dp));
        }else{
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_grid_black_24dp));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        Bundle bundle = new Bundle();
        if (itemId == R.id.audio_fragment) {
            bundle.putString("ListType", "audio");
            selectedFragment = new AudioVideoFoldersFragment();
            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();

        } else if (itemId == R.id.video_fragment) {
            bundle.putString("ListType", "video");
            selectedFragment = new AudioVideoFoldersFragment();
            selectedFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();

        }else if(itemId == R.id.other_fragment){
            startActivity(new Intent(this,WhatsappVideoList.class));
        }
        return true;
    }
}
