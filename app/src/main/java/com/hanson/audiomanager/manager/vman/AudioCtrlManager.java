package com.hanson.audiomanager.manager.vman;

import android.content.Context;
import android.util.Log;

import com.cvte.vman.VmanMiddleWare;
import com.hanson.audiomanager.AudioUtil;

public class AudioCtrlManager {
    private static final String TAG = "AudioCtrlManager";
    private static Context mContext;
    private static AudioCtrlManager instance;

    public AudioCtrlManager(Context context) {
        mContext = context;
    }

    public static AudioCtrlManager getInstance(Context context) {
        if (instance == null) {
            instance = new AudioCtrlManager(context);
        }
        return instance;
    }

    private Context getContext() {
        return mContext;
    }

    public int getHeadphoneMute() {
        int mute = VmanMiddleWare.getInstance(getContext()).getAudioCtrl().getHeadphoneMute();
        Log.d(TAG, "getHeadphoneMute: mute="+mute);
        return mute;
    }

    public boolean setHeadphoneMute(int status) {
        boolean ret = VmanMiddleWare.getInstance(getContext()).getAudioCtrl().setHeadphoneMute(status);
        Log.d(TAG, "setHeadphoneMute: status="+status+", ret="+ret);
        return ret;
    }

    public int getAudioAmpMute() {
        int mute = VmanMiddleWare.getInstance(getContext()).getAudioCtrl().getAudioAmpMute(1);
        Log.d(TAG, "getAudioAmpMute: mute="+mute);
        return mute;
    }

    public boolean setAudioAmpMute(int isMuted) {
        boolean ret = VmanMiddleWare.getInstance(getContext()).getAudioCtrl().setHeadphoneMute(isMuted);
        Log.d(TAG, "setHeadphoneMute: isMuted="+isMuted+", ret="+ret);
        return ret;
    }
}
