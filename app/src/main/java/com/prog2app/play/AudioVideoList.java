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

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

public class AudioVideoList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> audiosVideosUri = new ArrayList<>();
    ArrayList<String> audiosVideosTitle= new ArrayList<>();
    ArrayList<String> audiosVideosDuration= new ArrayList<>();
    boolean linearLayout;
    AudioVideoListAdapter audioVideoListAdapter =new AudioVideoListAdapter();
    private Menu menu;
    //new lib for duration
    ColumnIndexCache cache = new ColumnIndexCache();
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_list);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String folderName=getIntent().getStringExtra("FolderName");
        setTitle(folderName);
        audioVideoListAdapter.setFolderName(folderName);
        SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
        linearLayout = prefs.getBoolean("videosLinearLayout", true);
        String listType=getIntent().getStringExtra("ListType");
        if( listType.equals("video")){
            audioVideoListAdapter.setListType(listType);
            fetchVideos(folderName);
        }else{
            fetchAudios(folderName);
            audioVideoListAdapter.setListType(listType);

        }


    }
    private void fetchAudios(String folderName) {
        Uri uri;
        String path;
        String[] splits;
        int column_index_data,thum,title;
        String duration;
        Cursor cursor;
        uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media._ID, MediaStore.Images.Thumbnails.DATA};
        //String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        //testing
        String selection=MediaStore.Audio.Media.DATA +" like?";
        String[] selectedFolder=new String[]{"%"+folderName+"%"};
        cursor=getApplicationContext().getContentResolver().query(uri,projection,selection,selectedFolder,null);
        //testing

        column_index_data=cache.getColumnIndex(cursor,MediaStore.MediaColumns.DATA);
        title=cache.getColumnIndex(cursor,MediaStore.Audio.Media.DISPLAY_NAME);
        while (cursor.moveToNext()){
            path=cursor.getString(column_index_data);
            splits = path.split("/");
            if(path.endsWith("mp3") || path.endsWith("m4a") && folderName.equals(splits[splits.length - 2])){
            audiosVideosUri.add(cursor.getString(column_index_data));
            audiosVideosTitle.add(cursor.getString(title));
            duration=cursor.getString(cache.getColumnIndex(cursor,MediaStore.Audio.Media.DURATION));
            if(duration==null){
                retriever.setDataSource(cursor.getString(column_index_data));
                duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                audiosVideosDuration.add(duration);
            }else {
                audiosVideosDuration.add(duration);
            }
        }}
        // Clear the cache after you're done
        cache.clear();
        retriever.release();

        audioVideoListAdapter.setValues(audiosVideosUri,audiosVideosTitle,audiosVideosDuration,this);
        checkLayout();
        nativeAd();
    }
    private void fetchVideos(String folderName) {
        Uri uri;
        int column_index_data,title;
        String duration;
        Cursor cursor;
        String path;
        String[] splits;
        uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION};
        String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        //testing
        String selection=MediaStore.Video.Media.DATA +" like?";
        String[] selectedFolder=new String[]{"%"+folderName+"%"};
        cursor=getApplicationContext().getContentResolver().query(uri,projection,selection,selectedFolder,orderBy+" DESC");
        //testing

        column_index_data=cache.getColumnIndex(cursor,MediaStore.MediaColumns.DATA);
        title=cache.getColumnIndex(cursor,MediaStore.Video.Media.DISPLAY_NAME);

        while (cursor.moveToNext()){
            path=cursor.getString(column_index_data);
            splits = path.split("/");
//            if(splits[splits.length - 2].equals("") || splits[splits.length - 2].equals(null)){
//                splits[splits.length - 2]=splits[splits.length - 3];
//            }
            if(folderName.equals(splits[splits.length - 2])) {
                audiosVideosUri.add(cursor.getString(column_index_data));
                audiosVideosTitle.add(cursor.getString(title));
                duration = cursor.getString(cache.getColumnIndex(cursor, MediaStore.Video.Media.DURATION));
                if (duration == null) {
                    retriever.setDataSource(cursor.getString(column_index_data));
                    duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    audiosVideosDuration.add(duration);
                } else {
                    audiosVideosDuration.add(duration);
                }
            }

        }
        // Clear the cache after you're done
        cache.clear();
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
                        audioVideoListAdapter.setValues(audiosVideosUri,audiosVideosTitle,audiosVideosDuration,AudioVideoList.this,styles,unifiedNativeAd);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_list_toolbar_menu,menu);
        this.menu=menu;
        changeLayoutIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent viewIntent;
        switch (item.getItemId()){
            case R.id.folderLayout:
                checkLayout();
                changeLayoutIcon();
                SharedPreferences.Editor editor = getSharedPreferences("Log", MODE_PRIVATE).edit();
                if (linearLayout) {
                    editor.putBoolean("videosLinearLayout", false);
                } else {
                    editor.putBoolean("videosLinearLayout", true);
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
        audioVideoListAdapter.setLayout(linearLayout);
        if (linearLayout) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(audioVideoListAdapter);

        } else {
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
        linearLayout=!linearLayout;
    }
    public class ColumnIndexCache {
        private ArrayMap<String, Integer> mMap = new ArrayMap<>();
        public int getColumnIndex(Cursor cursor, String columnName) {
            if (!mMap.containsKey(columnName))
                mMap.put(columnName, cursor.getColumnIndex(columnName));
            return mMap.get(columnName);
        }   public void clear() {
            mMap.clear();
        }
    }

}
