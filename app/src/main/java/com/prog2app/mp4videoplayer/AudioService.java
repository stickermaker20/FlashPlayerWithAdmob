package com.prog2app.mp4videoplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class AudioService extends Service {

    MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
         mediaPlayer=new MediaPlayer();
        String audioPath=intent.getStringExtra("audioPath");
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int audioCurrentTime=intent.getIntExtra("audioTime", 0);
        mediaPlayer.seekTo(audioCurrentTime);
        mediaPlayer.start();
    return START_STICKY;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

}
