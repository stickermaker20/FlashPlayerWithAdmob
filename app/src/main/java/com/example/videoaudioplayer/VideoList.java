package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import wseemann.media.FFmpegMediaMetadataRetriever;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class VideoList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> videosUri = new ArrayList<>();
    ArrayList<String> videosTitle= new ArrayList<>();
    //ArrayList<String> videosThumb= new ArrayList<>();
    ArrayList<String> videosDuration= new ArrayList<>();
    //new lib for duration
    MediaMetadataRetriever mMediaMetadataRetriever = new MediaMetadataRetriever();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String folderName=getIntent().getStringExtra("FolderName");
        setTitle(folderName);
        fetchVideos(folderName);





    }
    private void fetchVideos(String folderName) {
        Uri uri;
        int column_index_data,title;//thum,duration;
        String mVideoDuration;
        Cursor cursor;
        uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.DATA, MediaStore.Video.VideoColumns.DURATION, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};
        String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        //testing
        String selection=MediaStore.Video.Media.DATA +" like?";
        String[] selectedFolder=new String[]{"%"+folderName+"%"};
        cursor=getApplicationContext().getContentResolver().query(uri,projection,selection,selectedFolder,orderBy+" DESC");
        //testing

        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        title=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
        //duration=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        //thum=cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        while (cursor.moveToNext()){
            videosUri.add(cursor.getString(column_index_data));
            //videosThumb.add(cursor.getString(thum));
            videosTitle.add(cursor.getString(title));
            //new code for duration
            mMediaMetadataRetriever.setDataSource(cursor.getString(column_index_data));
            mVideoDuration = mMediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            videosDuration.add(mVideoDuration);

        }
        recyclerView.setAdapter(new VideoListAdapter(videosUri,videosTitle,videosDuration,VideoList.this));
        nativeAd();
    }
    private  void nativeAd(){
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        recyclerView.setAdapter(new VideoListAdapter(videosUri,videosTitle,videosDuration,VideoList.this,styles,unifiedNativeAd));
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
