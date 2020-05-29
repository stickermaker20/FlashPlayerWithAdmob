package com.prog2app.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class VideoFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> folderName= new ArrayList<>();
    ArrayList<Integer> totalVideos= new ArrayList<>();
    boolean linearLayout;
    VideoFoldersAdapter videoFoldersAdapter=new VideoFoldersAdapter();
    private Menu menu;
    ColumnIndexCache cache = new ColumnIndexCache();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_folder);
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

        SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
        linearLayout = prefs.getBoolean("foldersLinearLayout", true);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        fetchVideoFolders();
    }

    private void fetchVideoFolders() {
        Uri uri;

        int folderNameInt;
        Cursor cursor;
        uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,MediaStore.MediaColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};
        String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        cursor=getApplicationContext().getContentResolver().query(uri,projection,null,null,orderBy+" DESC");

        folderNameInt=cache.getColumnIndex(cursor,MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            folderName.add(cursor.getString(folderNameInt));
        }
// Clear the cache after you're done
        cache.clear();



        LinkedHashSet<String> hashSet=new LinkedHashSet<>(folderName);
        final ArrayList<String> singleFolderName=new ArrayList<>(hashSet);
        videocount(singleFolderName);


//        recyclerView.setAdapter(new VideoFoldersAdapter(singleFolderName,totalVideos,VideoFolderActivity.this));
        videoFoldersAdapter.setValues(singleFolderName,totalVideos,VideoFolderActivity.this);
        checkLayout();
        nativeAd(singleFolderName);
    }

    public void videocount(ArrayList<String> singleFolderName){
        for(int i=0;i<singleFolderName.size();i++){
            Cursor checkCursor;
            int count=0;
            String[] checkProjection={MediaStore.MediaColumns.DATA};
            String checkOrderBy=MediaStore.Images.Media.DATE_TAKEN;
            String selection=MediaStore.Video.Media.DATA +" like?";
            String[] selectedFolder=new String[]{"%"+singleFolderName.get(i)+"%"};
            checkCursor=getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,checkProjection,selection,selectedFolder,checkOrderBy+" DESC");
            while (checkCursor.moveToNext()){
                count++;


            }
            totalVideos.add(count);
        }}

    private  void nativeAd(final ArrayList<String> singleFolderName){
        MobileAds.initialize(this, "ca-app-pub-2758711540829766~2235587113");
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-2758711540829766/2923705024")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
//                        recyclerView.setAdapter(new VideoFoldersAdapter(singleFolderName,totalVideos,VideoFolderActivity.this,styles,unifiedNativeAd));
                        videoFoldersAdapter.setValues(singleFolderName,totalVideos,VideoFolderActivity.this,styles,unifiedNativeAd);
                        recyclerView.setAdapter(videoFoldersAdapter);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        //Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
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
                checkLayout();
                changeLayoutIcon();
                SharedPreferences.Editor editor = getSharedPreferences("Log", MODE_PRIVATE).edit();

                if (linearLayout) {
                    editor.putBoolean("foldersLinearLayout", false);
                } else {
                    editor.putBoolean("foldersLinearLayout", true);
                }
                editor.commit();
                break;
        }
        return true;
    }

    private void changeLayoutIcon() {
        if(linearLayout){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_linear_black_24dp));
        }else{
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_grid_black_24dp));
        }
    }

    public void checkLayout() {
        videoFoldersAdapter.setLayout(linearLayout);
        if (linearLayout) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(videoFoldersAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch(videoFoldersAdapter.getItemViewType(position)){
                        case 0:
                            return 1;
                        case 1:
                            return 4;

                        default:
                            return 1;


                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(videoFoldersAdapter);
        }
        linearLayout=!linearLayout;
    }
    public class ColumnIndexCache {
        private ArrayMap<String, Integer> mMap = new ArrayMap<>();   public int getColumnIndex(Cursor cursor, String columnName) {
            if (!mMap.containsKey(columnName))
                mMap.put(columnName, cursor.getColumnIndex(columnName));
            return mMap.get(columnName);
        }   public void clear() {
            mMap.clear();
        }
    }

}
