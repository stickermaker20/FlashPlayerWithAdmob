package com.prog2app.play;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class VideoFolderFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> folderName= new ArrayList<>();
    ArrayList<Integer> totalVideos= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_video_folder, container, false);

        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchVideoFolders();

        return view;




    }
    private void fetchVideoFolders() {
        Uri uri;

        int column_index_data,thum,title,folderNameInt;
        Cursor cursor,checkCursor;
        String absolutePathImage=null;
        uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,MediaStore.MediaColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA};
        String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        //testing
//        String selection=MediaStore.Video.Media.DATA +" like?";
//        String[] selectedFolder=new String[]{"%Movies%"};
        cursor=getContext().getContentResolver().query(uri,projection,null,null,orderBy+" DESC");

        folderNameInt=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);

//        column_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        title=cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//        thum=cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        while (cursor.moveToNext()){
            folderName.add(cursor.getString(folderNameInt));
//            videoThum.add(cursor.getString(thum));
//            videoTitle.add(cursor.getString(title));

        }

        LinkedHashSet<String> hashSet=new LinkedHashSet<>(folderName);
        ArrayList<String> singleFolderName=new ArrayList<>(hashSet);

        videocount(singleFolderName);
        recyclerView.setAdapter(new VideoFoldersAdapter(singleFolderName,totalVideos,getActivity()));
    }

    public void videocount(ArrayList<String> singleFolderName){
        for(int i=0;i<singleFolderName.size();i++){
            Cursor checkCursor;
            int count=0;
            String[] checkProjection={MediaStore.MediaColumns.DATA};
            String checkOrderBy=MediaStore.Images.Media.DATE_TAKEN;
            String selection=MediaStore.Video.Media.DATA +" like?";
            String[] selectedFolder=new String[]{"%"+singleFolderName.get(i)+"%"};
            checkCursor=getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,checkProjection,selection,selectedFolder,checkOrderBy+" DESC");
            while (checkCursor.moveToNext()){
                count++;


            }
            totalVideos.add(count);
        }}
    }
