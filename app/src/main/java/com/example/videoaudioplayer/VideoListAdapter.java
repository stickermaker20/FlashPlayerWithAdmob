package com.example.videoaudioplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    ArrayList<String> videosUri;
    //ArrayList<String> videosThumb;
    ArrayList<String> videosTitle;
    ArrayList<String> videosDuration;
    Bitmap bmThumbnail;
    Activity activity;
    NativeTemplateStyle styles;
    UnifiedNativeAd unifiedNativeAd;
    int showingPosition=1;
    public VideoListAdapter(ArrayList<String> videosUri, ArrayList<String> videosTitle, ArrayList<String> videosDuration, Activity activity) {
        this.videosUri=videosUri;
        this.videosTitle=videosTitle;
        //this.videosThumb=videosThumb;
        this.videosDuration=videosDuration;
        this.activity=activity;

    }

    public VideoListAdapter(ArrayList<String> videosUri, ArrayList<String> videosTitle, ArrayList<String> videosDuration, Activity activity, NativeTemplateStyle styles, UnifiedNativeAd unifiedNativeAd) {
        this.videosUri=videosUri;
        this.videosTitle=videosTitle;
        //this.videosThumb=videosThumb;
        this.videosDuration=videosDuration;
        this.activity=activity;
        this.styles=styles;
        this.unifiedNativeAd=unifiedNativeAd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_layout, parent, false);
            return new NativeAdViewHolder(view);

        }else if(viewType == 2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_ad_layout, parent, false);
            return new BannerAdViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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
                    holder.videoName.setText(videosTitle.get(position-showingPosition));
                    holder.videoDescription.setText(getTimeString(Integer.parseInt(videosDuration.get(position-showingPosition))));

                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(videosUri.get(position-showingPosition), MediaStore.Video.Thumbnails.MICRO_KIND);
                    bmThumbnail = ThumbnailUtils.extractThumbnail(bmThumbnail, 970, 580);
                    holder.imageView.setImageBitmap(bmThumbnail);
                    holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, ViewVideo.class);
                            intent.putStringArrayListExtra("VideoUri", videosUri);
                            intent.putStringArrayListExtra("VideoTitle", videosTitle);
                            intent.putExtra("VideoPosition", position-showingPosition);
                            activity.startActivity(intent);
                        }
                    });

            }else{
                holder.videoName.setText(videosTitle.get(position));
                holder.videoDescription.setText(getTimeString(Integer.parseInt(videosDuration.get(position))));

                bmThumbnail = ThumbnailUtils.createVideoThumbnail(videosUri.get(position), MediaStore.Video.Thumbnails.MICRO_KIND);
                bmThumbnail = ThumbnailUtils.extractThumbnail(bmThumbnail, 970, 580);
                holder.imageView.setImageBitmap(bmThumbnail);
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ViewVideo.class);
                        intent.putStringArrayListExtra("VideoUri", videosUri);
                        intent.putStringArrayListExtra("VideoTitle", videosTitle);
                        intent.putExtra("VideoPosition", position);
                        activity.startActivity(intent);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if(styles !=null){
            if(videosTitle.size()<=5){
                int v=videosTitle.size()+1;
                return  v;
            }else{
                int v=(videosTitle.size()-5)/8;
                int x=videosTitle.size()+1+v;
                return  x;
            }
        }else{
            return videosTitle.size();
        }

    }
    @Override
    public int getItemViewType(int position) {
        if (styles !=null) {
            if (videosTitle.size() <= 5) {
                if (position == videosTitle.size()) {
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
        ImageView imageView;
        RelativeLayout relativeLayout;
        TextView videoName,videoDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            imageView=(ImageView) itemView.findViewById(R.id.videoThumbnail);
            videoName=(TextView) itemView.findViewById(R.id.videoName);
            videoDescription=(TextView) itemView.findViewById(R.id.videoDescription);
        }
    }
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        if(hours!=0){
        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));
        }else{
            buf
                    .append(String.format("%02d", minutes))
                    .append(":")
                    .append(String.format("%02d", seconds));
        }

        return buf.toString();
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
