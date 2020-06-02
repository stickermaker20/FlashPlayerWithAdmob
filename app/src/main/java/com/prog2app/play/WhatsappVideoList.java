package com.prog2app.play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.io.File;
import java.util.ArrayList;

public class WhatsappVideoList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> audiosVideosUri = new ArrayList<>();
    ArrayList<String> audiosVideosTitle= new ArrayList<>();
    ArrayList<String> audiosVideosDuration= new ArrayList<>();
    boolean linearLayout=false;
    AudioVideoListAdapter audioVideoListAdapter =new AudioVideoListAdapter();
    private Menu menu;
    //new lib for duration
    ColumnIndexCache cache = new ColumnIndexCache();
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_video_list);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setTitle("Seen WhatsApp Status");
        audioVideoListAdapter.setFolderName("Folder for Whatsapp Status Saving");
        audioVideoListAdapter.setListType("video");
        displayTreeStructure("/storage/emulated/0/WhatsApp/Media/.Statuses");

    }
    private void displayTreeStructure(String whatsappMediaDirectoryName) {
        File whatsappMediaDirectory = new File(whatsappMediaDirectoryName);
        File[] mediaDirectories = whatsappMediaDirectory.listFiles();
            for (File mediaDirectory : mediaDirectories) {
                if (mediaDirectory.getName().endsWith(".mp4")) {
                    audiosVideosUri.add(mediaDirectory.getAbsolutePath());
                    retriever.setDataSource(mediaDirectory.getAbsolutePath());
                    audiosVideosDuration.add(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    audiosVideosTitle.add(mediaDirectory.getName());
                }
            }
        retriever.release();
        audioVideoListAdapter.setValues(audiosVideosUri,audiosVideosTitle,audiosVideosDuration,this);
        checkLayout();
        nativeAd();
    }
    private  void nativeAd(){
        MobileAds.initialize(this, "ca-app-pub-2758711540829766~2235587113");
        //native test ad id = ca-app-pub-3940256099942544/2247696110
        //native ad id=  ca-app-pub-2758711540829766/2923705024
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
                        addingAdsItems();
//                        recyclerView.setAdapter(new AudioVideoListAdapter(videosUri,videosTitle,videosDuration,VideoList.this,styles,unifiedNativeAd));
                        audioVideoListAdapter.setValues(audiosVideosUri,audiosVideosTitle,audiosVideosDuration,WhatsappVideoList.this,styles,unifiedNativeAd);
                        recyclerView.setAdapter(audioVideoListAdapter);
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
    private void addingAdsItems() {
        if(audiosVideosTitle.size()<=4){
            audiosVideosTitle.add("native add");
            audiosVideosUri.add("native add");
            audiosVideosDuration.add("native add");
        }else{
            audiosVideosTitle.add(4,"native add");
            audiosVideosUri.add(4,"native add");
            audiosVideosDuration.add(4,"native add");
            for(int i=0;i<audiosVideosTitle.size();i++){
                if(i==13 || i == 22 || i == 31 || i == 40 || i == 49) {
                    audiosVideosTitle.add(i,"banner add");
                    audiosVideosUri.add(i,"banner add");
                    audiosVideosDuration.add(i,"banner add");
                }
            }
        }
    }

    public void checkLayout() {
        audioVideoListAdapter.setLayout(linearLayout);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch(audioVideoListAdapter.getItemViewType(position)){
                        case 0:
                            return 1;
                        case 1:
                            return 2;
                        case 2:
                            return 2;

                        default:
                            return 1;


                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(audioVideoListAdapter);

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
