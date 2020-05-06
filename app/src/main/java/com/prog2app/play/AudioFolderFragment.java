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

public class AudioFolderFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> folderName= new ArrayList<>();
    ArrayList<Integer> totalAudios= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_audio_folder, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchAudioFolders();
        return view;






    }
    private void fetchAudioFolders() {
        Uri uri;

        int folderNameInt;
        Cursor cursor;
        uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.Audio.Media.ALBUM};
        //String orderBy=MediaStore.Images.Media.DATE_TAKEN;
        // String orderBy=MediaStore.Images.Media.DATE_TAKEN;

        cursor=getContext().getContentResolver().query(uri,projection,  null,null,null);

        folderNameInt=cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);

        while (cursor.moveToNext()){
            folderName.add(cursor.getString(folderNameInt));
        }

        LinkedHashSet<String> hashSet=new LinkedHashSet<>(folderName);
        ArrayList<String> singleFolderName=new ArrayList<>(hashSet);

        audiocount(singleFolderName);
//        singleFolderName.remove(2);
//        for(int x=0; x<singleFolderName.size();x++){
//
//            if(totalAudios.get(x)==0){
//             singleFolderName.remove(x);
//             totalAudios.remove(x);
//            }
//        }
        recyclerView.setAdapter(new AudioFoldersAdapter(singleFolderName,totalAudios,getContext()));
    }

    public void audiocount(ArrayList<String> singleFolderName){
        for(int i=0;i<singleFolderName.size();i++){
            Cursor checkCursor;
            int count=0;
            String[] checkProjection={MediaStore.Audio.Media.DISPLAY_NAME};
            // String checkOrderBy=MediaStore.Images.Media.DATE_TAKEN;
            String selection=MediaStore.Audio.Media.DATA +" like?";
            String[] selectedFolder=new String[]{"%"+singleFolderName.get(i)+"%"};
            checkCursor=getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,checkProjection,selection,selectedFolder,null);
            while (checkCursor.moveToNext()){
                count++;


            }
            totalAudios.add(count);
            }}
}
