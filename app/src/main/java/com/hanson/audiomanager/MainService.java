package com.hanson.audiomanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {
    private static final String TAG = "MainService";

    private MediaPlayer player;

    private AudioManager mAm;

    private static MyOnAudioFocusChangeListener mListener;
    NotificationChannel notificationChannel;
    Notification notification;
    private static final int DefaultNotifyID = 1;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        player = MediaPlayer.create(this, R.raw.time); // 在res目录下新建raw目录，复制一个test.mp3文件到此目录下。
        player.setLooping(false);

        mAm = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mListener = new MyOnAudioFocusChangeListener();
        createNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();
        startForeground(DefaultNotifyID, notification);
        Log.d(TAG, "onStartCommand");

        // Request audio focus for playback
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_SYSTEM, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(TAG, "requestAudioFocus successfully.");

            // Start playback.
            player.start();
        } else {
            Log.e(TAG, "requestAudioFocus failed.");
        }
        stopForeground(false);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        player.stop();

        mAm.abandonAudioFocus(mListener);
        deleteNotification();
    }

    private class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "focusChange=" + focusChange);
        }
    }

    private void createNotification(){
        notificationChannel = new NotificationChannel("AudioManager",
                "AudioManager", NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);
        if (notificationChannel != null){
            Log.i(TAG,"Build the notification in Channel: " + notificationChannel.getId());
            notification = new Notification.Builder(this, notificationChannel.getId()).build();
        }
    }

    private void deleteNotification(){
        if (notificationChannel != null) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.deleteNotificationChannel(notificationChannel.getId());
            notificationChannel = null;
        }
        notification = null;
    }
}