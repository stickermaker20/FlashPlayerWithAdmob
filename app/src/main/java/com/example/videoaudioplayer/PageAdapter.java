package com.example.videoaudioplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int countTab;
    public PageAdapter(@NonNull FragmentManager fm,int countTab) {
        super(fm);
        this.countTab=countTab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                VideoFolderFragment videoFolderFragment=new VideoFolderFragment();
                return videoFolderFragment;
            case 1:
                AudioFolderFragment audioFolderFragment=new AudioFolderFragment();
                return audioFolderFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
