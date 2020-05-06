package com.prog2app.play;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AudioFoldersAdapter extends RecyclerView.Adapter<AudioFoldersAdapter.ViewHolder> {
    ArrayList<String> folderName;
    ArrayList <Integer> totalAudio;
    Context context;


    public AudioFoldersAdapter(ArrayList<String> folderName,ArrayList<Integer> totalAudio, Context applicationContext){
        this.folderName=folderName;
        this.totalAudio=totalAudio;
        this.context=applicationContext;
    }
    @NonNull
    @Override
    public AudioFoldersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_folders_items,parent,false);
        return new AudioFoldersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.folderName.setText(folderName.get(position));
        holder.totalAudio.setText(totalAudio.get(position)+" Audios");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AudioList.class);
                intent.putExtra("FolderName",folderName.get(position));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return folderName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView folderName,totalAudio;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            folderName=(TextView) itemView.findViewById(R.id.folderName);
            totalAudio=(TextView) itemView.findViewById(R.id.totalAudio);
        }
    }
}
