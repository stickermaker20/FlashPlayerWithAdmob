package com.prog2app.play;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AudioVideoFoldersFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> folderName= new ArrayList<>();
    ArrayList<Integer> totalAudiosVideos= new ArrayList<>();
    private boolean linearLayout;
    AudioVideoFoldersAdapter audioVideoFoldersAdapter =new AudioVideoFoldersAdapter();
    ColumnIndexCache cache = new ColumnIndexCache();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_audio_video_folders, container, false);
        String listType = getArguments().getString("ListType");
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("Log", MODE_PRIVATE);
        linearLayout = prefs.getBoolean("foldersLinearLayout", true);
//        linearLayout=getArguments().getBoolean("LinearLayout");
        checkLayout(linearLayout);
        if( listType==null || listType.equals("video") || listType.equals("") || listType.equals(null)){
            audioVideoFoldersAdapter.setListType("video");
            fetchVideoFolders();
        }else{
            audioVideoFoldersAdapter.setListType("audio");
            fetchAudioFolders();

        }

        return view;
    }
    private void fetchVideoFolders() {
        Uri uri= MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,MediaStore.MediaColumns.BUCKET_ID};

        Cursor cursor=getContext().getContentResolver().query(uri,projection,null,null,null);

        int folderNameInt=cache.getColumnIndex(cursor,MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            folderName.add(cursor.getString(folderNameInt));
        }
        // Clear the cache after you're done
        cache.clear();

        LinkedHashSet<String> hashSet=new LinkedHashSet<>(folderName);
        ArrayList<String> singleFolderName=new ArrayList<>(hashSet);

        //sort foldernames alphabetical
        singleFolderName=sortFolderNames(singleFolderName);
        countVideos(singleFolderName);


        audioVideoFoldersAdapter.setValues(singleFolderName,totalAudiosVideos,getActivity());

        nativeAd(singleFolderName);
    }

    private ArrayList<String> sortFolderNames(ArrayList<String> folderName) {

        Collections.sort(folderName, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        return folderName;
    }

    private void fetchAudioFolders() {
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;;
        String path;
        int column_index_data,folderRepeat;
        Cursor cursor;
        String[] splits;
        String[] projection={MediaStore.Audio.Media.ALBUM,MediaStore.MediaColumns.DATA};

        cursor=getContext().getContentResolver().query(uri,projection,  null,null,null);

        column_index_data=cache.getColumnIndex(cursor,MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()){
            path=cursor.getString(column_index_data);
            splits = path.split("/");

            folderName.add(splits[splits.length-2]) ;//(cursor.getString(folderNameInt));


        }
        folderName=sortFolderNames(folderName);
        folderRepeat= Collections.frequency(folderName, folderName.get(0));
        totalAudiosVideos.add(folderRepeat);
        for(int i=1;i<folderName.size();i++){
            if(!folderName.get(i).equals(folderName.get(i-1)) ){
                folderRepeat= Collections.frequency(folderName, folderName.get(i));
                totalAudiosVideos.add(folderRepeat);
            }
        }
        LinkedHashSet<String> hashSet=new LinkedHashSet<>(folderName);
        ArrayList<String> singleFolderName=new ArrayList<>(hashSet);
        audioVideoFoldersAdapter.setValues(singleFolderName,totalAudiosVideos,getActivity());
//        checkLayout();
        nativeAd(singleFolderName);
    }

    public void countVideos(ArrayList<String> singleFolderName){
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
            totalAudiosVideos.add(count);
        }}

    private  void nativeAd(final ArrayList<String> singleFolderName){
        MobileAds.initialize(getActivity(), "ca-app-pub-2758711540829766~2235587113");
        //native test ad id = ca-app-pub-3940256099942544/2247696110
        //native ad id=  ca-app-pub-2758711540829766/2923705024
        AdLoader adLoader = new AdLoader.Builder(getActivity(), "ca-app-pub-3940256099942544/2247696110")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();
//                        recyclerView.setAdapter(new AudioVideoFoldersAdapter(singleFolderName,totalVideos,VideoFolderActivity.this,styles,unifiedNativeAd));
                        audioVideoFoldersAdapter.setValues(singleFolderName,totalAudiosVideos,getActivity(),styles,unifiedNativeAd);
                        recyclerView.setAdapter(audioVideoFoldersAdapter);
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
    public void checkLayout(boolean linearLayout) {
        audioVideoFoldersAdapter.setLayout(linearLayout);
        if (linearLayout) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(audioVideoFoldersAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch(audioVideoFoldersAdapter.getItemViewType(position)){
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
            recyclerView.setAdapter(audioVideoFoldersAdapter);
        }

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
