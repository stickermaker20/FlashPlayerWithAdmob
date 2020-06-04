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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DownloadService extends Service {
    private final String CHANNEL_ID ="personal" ;
    public final int NOTIFICATION_ID = 001;
    Thread thread;
    String title;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

         thread=new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Log", MODE_PRIVATE);
                title = sharedPreferences.getString("DownloadServiceTitle", "Downloading Video");
                while (title.equals("Downloading Video")){
                    try {
                        Thread.sleep(1000);
                        generateAlert(title);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences("Log", MODE_PRIVATE);
        title = sharedPreferences.getString("DownloadServiceTitle", "Download Ended");
        generateAlert(title);
    }

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    private void generateAlert(String title) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(title);

        NotificationManager notificationManager = (NotificationManager)getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify(NOTIFICATION_ID,builder.build() );

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "personal";
            String Description = "Please review time table";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel( CHANNEL_ID,name,importance );
            notificationChannel.setDescription( Description );

            NotificationManager notificationManager = (NotificationManager)getSystemService( NOTIFICATION_SERVICE );
            notificationManager.createNotificationChannel( notificationChannel );
        }
    }
}
