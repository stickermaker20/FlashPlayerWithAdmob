package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class VideoList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> videosUri = new ArrayList<>();
    ArrayList<String> videosTitle= new ArrayList<>();
    ArrayList<String> videosThumb= new ArrayList<>();
    ArrayList<String> videosDuration= new ArrayList<>();


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
        int column_index_data,thum,title,duration;
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
        duration=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
        thum=cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        while (cursor.moveToNext()){
            videosUri.add(cursor.getString(column_index_data));
            videosThumb.add(cursor.getString(thum));
            videosTitle.add(cursor.getString(title));
            videosDuration.add(cursor.getString(duration));

        }
        recyclerView.setAdapter(new VideoListAdapter(videosUri,videosThumb,videosTitle,videosDuration,getApplicationContext()));
    }
}
