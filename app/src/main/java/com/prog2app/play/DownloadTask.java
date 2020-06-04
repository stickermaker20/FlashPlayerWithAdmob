package com.prog2app.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private String downloadUrl = "", downloadFileName = "";

    public DownloadTask(Context context, String downloadUrl) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        downloadFileName = "facebook"+dateFormat.format(date)+".mp4";//Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName);

        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = new File(Environment.getExternalStorageDirectory().toString(),"Play/Downloaded Videos");
        File outputFile = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"Download Started",Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = context.getSharedPreferences("Log", MODE_PRIVATE).edit();
            editor.putString("DownloadServiceTitle", "Downloading Video");
            context.startService(new Intent(context , DownloadService.class));
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    Toast.makeText(context,"Download Completed",Toast.LENGTH_LONG).show();
//                    SharedPreferences.Editor editor = context.getSharedPreferences("Log", MODE_PRIVATE).edit();
//                    editor.putString("DownloadServiceTitle", "Video Download Finished");
                    context.stopService(new Intent(context , DownloadService.class));

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"Download Failed",Toast.LENGTH_LONG).show();
//                            SharedPreferences.Editor editor = context.getSharedPreferences("Log", MODE_PRIVATE).edit();
//                            editor.putString("DownloadServiceTitle", "Video Download Failed");
                            context.stopService(new Intent(context , DownloadService.class));
                        }
                    }, 3000);


                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context,"Download Failed with Exception - " + e.getMessage(),Toast.LENGTH_LONG).show();
                //Change button text if exception occurs
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,"Try again, download failed",Toast.LENGTH_LONG).show();
//                        SharedPreferences.Editor editor = context.getSharedPreferences("Log", MODE_PRIVATE).edit();
//                        editor.putString("DownloadServiceTitle", "Video Download Failed");
                        context.stopService(new Intent(context , DownloadService.class));
                    }
                }, 3000);


            }


            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context,c.getResponseCode() + " " + c.getResponseMessage(),Toast.LENGTH_LONG).show();
                }



                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    try {
                        outputFile.createNewFile();
                    }catch (Exception e){
                    }
                    Log.e(TAG, "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                int downloadSiz=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }
                //Close all connection after doing task
                fos.close();
                is.close();



            } catch (Exception e) {
        Toast.makeText(context,"Download Error Exception " + e.getMessage(),Toast.LENGTH_LONG).show();
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
//                SharedPreferences.Editor editor = context.getSharedPreferences("Log", MODE_PRIVATE).edit();
//                editor.putString("DownloadServiceTitle", "Video Download Failed");
                context.stopService(new Intent(context , DownloadService.class));
            }

            return null;
        }
    }
}
