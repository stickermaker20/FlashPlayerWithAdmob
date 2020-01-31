package com.example.videoaudioplayer;

import android.annotation.SuppressLint;
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

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class VideoFoldersAdapter extends RecyclerView.Adapter<VideoFoldersAdapter.ViewHolder> {
    ArrayList <String> folderName;
    ArrayList <Integer> totalVideo;
    Context context;


    public VideoFoldersAdapter(ArrayList<String> folderName,ArrayList<Integer> totalVideo, Context applicationContext){
        this.folderName=folderName;
        this.totalVideo=totalVideo;
        this.context=applicationContext;
    }
    @NonNull
    @Override
    public VideoFoldersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_folders_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoFoldersAdapter.ViewHolder holder, final int position) {
        holder.folderName.setText(folderName.get(position));
        holder.totalVideo.setText(totalVideo.get(position)+" Videos");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,VideoList.class);
                intent.putExtra("FolderName",folderName.get(position));
                context.startActivity(intent);
               //fragment

//                VideoListFragment videoListFragment=new VideoListFragment();
////
//                MainActivity myActivity = (MainActivity) context;
//                FragmentTransaction fragmentTransaction = myActivity.getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.pager, videoListFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
////                final ViewPager viewPager= (ViewPager) myActivity.findViewById(R.id.pager);
//////                TabLayout tabLayout=(TabLayout) myActivity.findViewById(R.id.tabLayout);
////                viewPager.setAdapter(new PageAdapter(myActivity.getSupportFragmentManager(),4));
////
////                viewPager.setCurrentItem(2);



            }
        });
    }

    @Override
    public int getItemCount() {
        return folderName.size();
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
}
