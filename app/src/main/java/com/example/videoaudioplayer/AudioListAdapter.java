package com.example.videoaudioplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileDescriptor;
import java.util.ArrayList;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {
    ArrayList<String> audiosUri;
    ArrayList<String> audiosThumb;
    ArrayList<String> audiosTitle;
    ArrayList<String> audiosDuration;
    Bitmap bmThumbnail;
    Context context;
    public AudioListAdapter(ArrayList<String> audiosUri, ArrayList<String> audiosThumb, ArrayList<String> audiosTitle, ArrayList<String> audiosDuration, Context applicationContext) {
        this.audiosUri=audiosUri;
        this.audiosTitle=audiosTitle;
        this.audiosThumb=audiosThumb;
        this.audiosDuration=audiosDuration;
        this.context=applicationContext;

    }


    @NonNull
    @Override
    public AudioListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item,parent,false);
        return new AudioListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.audioName.setText(audiosTitle.get(position));
        holder.audioDescription.setText(getTimeString(Integer.parseInt(audiosDuration.get(position))));

       // bmThumbnail = ThumbnailUtils.createAudioThumbnail(audiosUri.get(position), MediaStore.Images.Thumbnails.MICRO_KIND);
      //  bmThumbnail=GetImage(Uri.parse(audiosUri.get(position)),Integer.parseInt(audiosTitle.get(position)));
      //  holder.imageView.setImageBitmap(bmThumbnail);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewVideo.class);
                intent.putExtra("AudioUri",audiosUri.get(position));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return audiosTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout relativeLayout;
        TextView audioName,audioDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            imageView=(ImageView) itemView.findViewById(R.id.audioThumbnail);
            audioName=(TextView) itemView.findViewById(R.id.audioName);
            audioDescription=(TextView) itemView.findViewById(R.id.audioDescription);
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
//    private Bitmap GetImage(Uri uri, int album_id){
//
//        Bitmap bm = null;
//        try
//        {
//
//            Uri urii = ContentUris.withAppendedId(uri, album_id);
//
//            ParcelFileDescriptor pfd = context.getContentResolver()
//                    .openFileDescriptor(urii, "r");
//
//            if (pfd != null)
//            {
//                FileDescriptor fd = pfd.getFileDescriptor();
//                bm = BitmapFactory.decodeFileDescriptor(fd);
//            }
//        } catch (Exception e) {
//        }
//        return bm;
//    }

}

