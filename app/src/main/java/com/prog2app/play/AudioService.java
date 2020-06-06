package com.prog2app.play;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.VideoView;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
