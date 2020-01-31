package com.example.videoaudioplayer;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    ArrayList<String> videosUri;
    ArrayList<String> videosThumb;
    ArrayList<String> videosTitle;
    ArrayList<String> videosDuration;
    Bitmap bmThumbnail;
    Context context;
    public VideoListAdapter(ArrayList<String> videosUri, ArrayList<String> videosThumb, ArrayList<String> videosTitle, ArrayList<String> videosDuration, Context applicationContext) {
        this.videosUri=videosUri;
        this.videosTitle=videosTitle;
        this.videosThumb=videosThumb;
        this.videosDuration=videosDuration;
        this.context=applicationContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.videoName.setText(videosTitle.get(position));
        holder.videoDescription.setText(getTimeString(Integer.parseInt(videosDuration.get(position))));

       // holder.videoDescription.setText(videosDescription.get(position));
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(videosUri.get(position), MediaStore.Video.Thumbnails.MICRO_KIND);
        holder.imageView.setImageBitmap(bmThumbnail);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewVideo.class);
                intent.putExtra("VideoUri",videosUri.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videosTitle.size();
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

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
}
