package com.example.videoaudioplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class VideoFoldersAdapter extends RecyclerView.Adapter<VideoFoldersAdapter.ViewHolder> {
    ArrayList <String> folderName;
    ArrayList <Integer> totalVideo;
    Activity activity;
    NativeTemplateStyle styles;
    UnifiedNativeAd unifiedNativeAd;
    int showingPosition=1;


    public VideoFoldersAdapter(ArrayList<String> folderName,ArrayList<Integer> totalVideo, Activity activity){
        this.folderName=folderName;
        this.totalVideo=totalVideo;
        this.activity=activity;
    }

    public VideoFoldersAdapter(ArrayList<String> folderName, ArrayList<Integer> totalVideo, VideoFolderActivity activity, NativeTemplateStyle styles, UnifiedNativeAd unifiedNativeAd) {
        this.folderName=folderName;
        this.totalVideo=totalVideo;
        this.activity=activity;
        this.styles=styles;
        this.unifiedNativeAd=unifiedNativeAd;
    }

    @NonNull
    @Override
    public VideoFoldersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_layout, parent, false);
            return new NativeAdViewHolder(view);

        }else if(viewType == 2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_layout, parent, false);
            return new BannerAdViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_folders_items, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoFoldersAdapter.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
                NativeAdViewHolder new_holder = (NativeAdViewHolder) holder;
                        new_holder.template.setStyles(styles);
                        new_holder.template.setNativeAd(unifiedNativeAd);

        } else if (getItemViewType(position) == 2) {
            showingPosition=showingPosition+1;
            BannerAdViewHolder banner_new_holder = (BannerAdViewHolder) holder;
            banner_new_holder.mAdView.loadAd(new AdRequest.Builder().build());
        } else {
            if(position>5 && styles !=null){
                holder.folderName.setText(folderName.get(position-showingPosition));
                holder.totalVideo.setText(totalVideo.get(position-showingPosition) + " Videos");
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoList.class);
                        intent.putExtra("FolderName", folderName.get(position-showingPosition));
                        activity.startActivity(intent);


                    }
                });
            }else{
                holder.folderName.setText(folderName.get(position));
                holder.totalVideo.setText(totalVideo.get(position) + " Videos");
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoList.class);
                        intent.putExtra("FolderName", folderName.get(position));
                        activity.startActivity(intent);


                    }
                });
            }

        }
    }


    @Override
    public int getItemCount() {
        if(styles !=null){
            if(folderName.size()<=5){
                int v=folderName.size()+1;
                return  v;
            }else{
                int v=(folderName.size()-5)/8;
                int x=folderName.size()+1+v;
                return  x;
            }
        }else{
            return folderName.size();
        }

    }
    @Override
    public int getItemViewType(int position) {
        if (styles !=null) {
            if (folderName.size() <= 5) {
                if (position == folderName.size()) {
                    return 1;
                } else {
                    return 0;
                }

            }
            else {
                if (position == 5) {
                    return 1;
                } else if(position==14 || position == 23 || position == 32 || position == 41 || position == 50){
                    return 2;
                }
                else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName,totalVideo;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            folderName=(TextView) itemView.findViewById(R.id.folderName);
            totalVideo=(TextView) itemView.findViewById(R.id.totalVideo);
        }
    }
    public class NativeAdViewHolder extends ViewHolder {
        TemplateView template;
        public NativeAdViewHolder(@NonNull View itemView) {
            super(itemView);
            template = itemView.findViewById(R.id.my_template);
        }
    }
    public class BannerAdViewHolder extends ViewHolder {
        AdView mAdView;
        public BannerAdViewHolder(@NonNull View itemView) {
            super(itemView);

            mAdView = itemView.findViewById(R.id.adView);
        }
    }
}
