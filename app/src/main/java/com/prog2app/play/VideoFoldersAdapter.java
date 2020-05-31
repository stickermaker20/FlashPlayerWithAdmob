package com.prog2app.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class VideoFoldersAdapter extends RecyclerView.Adapter<VideoFoldersAdapter.ViewHolder> {
    ArrayList <String> folderName;
    ArrayList <Integer> totalVideo;
    Activity activity;
    boolean linearLayout;
    NativeTemplateStyle styles;
    UnifiedNativeAd unifiedNativeAd;


    public void setValues(ArrayList<String> folderName,ArrayList<Integer> totalVideo, Activity activity){
        this.folderName=folderName;
        this.totalVideo=totalVideo;
        this.activity=activity;
    }

    public void setValues(ArrayList<String> folderName, ArrayList<Integer> totalVideo, VideoFolderActivity activity, NativeTemplateStyle styles, UnifiedNativeAd unifiedNativeAd) {
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

        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(linearLayout? R.layout.video_folders_items : R.layout.grid_video_folders_items , parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoFoldersAdapter.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 1) {
                NativeAdViewHolder new_holder = (NativeAdViewHolder) holder;
                        new_holder.template.setStyles(styles);
                        new_holder.template.setNativeAd(unifiedNativeAd);

        } else {
            if(position>4 && styles !=null){
                if(!linearLayout){
                    StringTokenizer tokens = new StringTokenizer(folderName.get(position-1), " ");
                    holder.folderName.setText(tokens.nextToken());
                }else{
                    holder.folderName.setText(folderName.get(position-1));
                }
                holder.totalVideo.setText(totalVideo.get(position-1) + " Videos");
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoList.class);
                        intent.putExtra("FolderName", folderName.get(position-1));
                        activity.startActivity(intent);


                    }
                });
            }else{
                if(!linearLayout){
                    StringTokenizer tokens = new StringTokenizer(folderName.get(position), " ");
                    holder.folderName.setText(tokens.nextToken());
                }else{
                    holder.folderName.setText(folderName.get(position));
                }
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
            int v=folderName.size()+1;
            return  v;
        }else{
            return folderName.size();
        }

    }
    @Override
    public int getItemViewType(int position) {
        if (styles !=null) {
            if (folderName.size() <= 4) {
                if (position == folderName.size()) {
                    return 1;
                } else {
                    return 0;
                }

            }
            else {
                if (position == 4) {
                    return 1;
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
    public void setLayout(boolean newValue){
        linearLayout=newValue;
    }
}
