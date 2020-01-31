package com.example.videoaudioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;

public class AudioList extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> audiosUri = new ArrayList<>();
    ArrayList<String> audiosTitle= new ArrayList<>();
    ArrayList<String> audiosThumb= new ArrayList<>();
    ArrayList<String> audiosDuration= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String folderName=getIntent().getStringExtra("FolderName");
        setTitle(folderName);
        fetchAudios(folderName);





    }
    private void fetchAudios(String folderName) {
        Uri uri;
        int column_index_data,thum,title,duration;
        Cursor cursor;
        uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.DURATION, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media._ID, MediaStore.Images.Thumbnails.DATA};
        //String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        //testing
        String selection=MediaStore.Audio.Media.DATA +" like?";
        String[] selectedFolder=new String[]{"%"+folderName+"%"};
        cursor=getApplicationContext().getContentResolver().query(uri,projection,selection,selectedFolder,null);
        //testing

        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        title=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
        duration=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        thum=cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
        while (cursor.moveToNext()){
            audiosUri.add(cursor.getString(column_index_data));
            audiosThumb.add(cursor.getString(thum));
            audiosTitle.add(cursor.getString(title));
            audiosDuration.add(cursor.getString(duration));

        }
        recyclerView.setAdapter(new AudioListAdapter(audiosUri,audiosThumb,audiosTitle,audiosDuration,getApplicationContext()));
    }
}
