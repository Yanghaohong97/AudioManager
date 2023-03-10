package com.hanson.audiomanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MicrophoneInfo;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AudioUtil {
    private static final String TAG = "AudioUtil";
    private static Context mContext;
    private static AudioUtil instance;
    private static AudioManager mAudioManager = null;
    private static AudioDeviceInfo audioDeviceInfo;
    private static MicrophoneInfo[] microphones = new ArrayList<MicrophoneInfo>().toArray(new MicrophoneInfo[0]);


    public static AudioUtil getInstance(Context context) {
        if (instance == null) {
            instance = new AudioUtil(context);
        }
        return instance;
    }

    public AudioUtil(Context context) {
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        String[] strings = {"1", "2"};
        int num = strings.length;
    }

    public String getAudioInCardList() {
        Class audioManager;
        String selectActiveInCardIDList = "";
        Method method = null;
        int tmp = -1;
        boolean b = true;
        try {
            audioManager = Class.forName("android.media.AudioManager");
            method = audioManager.getDeclaredMethod("getAudioInCardList");
            selectActiveInCardIDList = (String) method.invoke(mAudioManager);
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }
        Log.i(TAG, "getAudioInCardList: " + selectActiveInCardIDList + ", tmp=" + tmp + ", b=" + b);
        return selectActiveInCardIDList;
    }

    public List<MicrophoneInfo> getMicrophones() {
        Class audioManager;
        List<MicrophoneInfo> microphoneInfos = null;
        Method method = null;
        try {
            audioManager = Class.forName("android.media.AudioManager");
            method = audioManager.getDeclaredMethod("getMicrophones");
            microphoneInfos = (ArrayList<MicrophoneInfo>) method.invoke(mAudioManager);
            int index = 0;
            Log.d(TAG, "----------------------------inputs------------------------");
            for (MicrophoneInfo microphoneInfo : microphoneInfos) {
                if (microphoneInfo != null) {
//                    selectActiveInCardIDList.append(index+". "+microphoneInfo.getType()+" "+microphoneInfo.getIndexInTheGroup()+" "+microphoneInfo.getDescription()+" "+microphoneInfo.getId()+"; ");
                    Log.d(TAG, "inputs: (" + index + ") type=" + microphoneInfo.getType() + ", address=" + microphoneInfo.getAddress()  + ", id=" + microphoneInfo.getId() + " description=" + microphoneInfo.getDescription() + "; ");
                    index++;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }
        return microphoneInfos;
    }

    public AudioDeviceInfo[] getDevices(int type) {
        Class audioManager;
        Method method = null;
        String typeName = "outputs";
        AudioDeviceInfo[] outputs = null;
        try {
            audioManager = Class.forName("android.media.AudioManager");
            method = audioManager.getDeclaredMethod("getDevices",int.class);
            outputs = (AudioDeviceInfo[]) method.invoke(mAudioManager,type);
            int index = 0;
            if (type == AudioManager.GET_DEVICES_INPUTS) {
                typeName = "inputs";
            }
            Log.d(TAG, "----------------------------"+typeName+"------------------------");
            for (AudioDeviceInfo output : outputs) {
                if (output != null) {
//                    selectActiveOutCardIDList.append(output.getAddress()+" "+output.getProductName()+" "+output.getId()+"; ");
                    Log.d(TAG, typeName+": (" + index + ") type="+output.getType()+"address=" + output.getAddress() + ", ProductName=" + output.getProductName() + "; ");
                    index++;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }
        return outputs;
    }


    public MicrophoneInfo getForceMicroPhone() {
        MicrophoneInfo microphoneInfo = null;
        try {
            Method method = AudioManager.class.getDeclaredMethod("getForceMicroPhone");
            method.setAccessible(true);
            microphoneInfo = (MicrophoneInfo) method.invoke(mAudioManager);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
        if (microphoneInfo != null) {
            Log.d(TAG, "getForceMicroPhone: getId=" + microphoneInfo.getId() + ", getDescription=" + microphoneInfo.getDescription() + ",getAddress=" + microphoneInfo.getAddress());
        }
        return microphoneInfo;
    }

    public AudioDeviceInfo getForceUseOutputDevice() {
        AudioDeviceInfo audioDeviceInfo = null;
        try {
            Method method = AudioManager.class.getMethod("getForceUseOutputDevice");
            method.setAccessible(true);
            audioDeviceInfo = (AudioDeviceInfo) method.invoke(mAudioManager);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
        if (audioDeviceInfo != null) {
            Log.d(TAG, "getForceUseOutputDevice: type="+audioDeviceInfo.getType()+",address="+audioDeviceInfo.getAddress()
                    +", ProductName" + audioDeviceInfo.getProductName());
        } else {
            Log.d(TAG, "getForceUseOutputDevice: audioDeviceInfo is null");
        }
        return audioDeviceInfo;
    }

    public void setForceMicroPhone(MicrophoneInfo microphoneInfo) {
        try {
            Method method = AudioManager.class.getMethod("setForceMicroPhone", MicrophoneInfo.class);
            method.invoke(mAudioManager, microphoneInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
    }

    public void setForceUseOutputDevice(AudioDeviceInfo audioDeviceInfo) {
        try {
            Method method = AudioManager.class.getMethod("setForceUseOutputDevice", AudioDeviceInfo.class);
            method.invoke(mAudioManager, audioDeviceInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
    }

}
