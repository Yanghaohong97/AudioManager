package com.hanson.audiomanager;


import android.content.Context;
import android.media.AudioCard;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MicrophoneInfo;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AudioUtilNew {
    private static final String TAG = "AudioUtilNew";
    private static Context mContext;
    private static AudioUtilNew instance;
    private static AudioManager mAudioManager = null;
    private static AudioDeviceInfo audioDeviceInfo;
    private static MicrophoneInfo[] microphones = new ArrayList<MicrophoneInfo>().toArray(new MicrophoneInfo[0]);


    public static AudioUtilNew getInstance(Context context) {
        if (instance == null) {
            instance = new AudioUtilNew(context);
        }
        return instance;
    }

    public AudioUtilNew(Context context) {
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        String[] strings = {"1", "2"};
        int num = strings.length;
    }

    public List<AudioCard> getAudioInCardList() {
        List<AudioCard> microphoneInfos = null;
        Class audioManager;
        Method method = null;
        try {
            audioManager = Class.forName("android.media.AudioManager");
            method = audioManager.getDeclaredMethod("getAudioInCardList");
            microphoneInfos = (List<AudioCard>) method.invoke(mAudioManager);
            int index = 0;
            Log.d(TAG, "----------------------------inputs------------------------");
            for (AudioCard audioCard : microphoneInfos) {
                if (audioCard != null) {
//                    selectActiveInCardIDList.append(index+". "+microphoneInfo.getType()+" "+microphoneInfo.getIndexInTheGroup()+" "+microphoneInfo.getDescription()+" "+microphoneInfo.getId()+"; ");
                    Log.d(TAG, "inputs: (" + index + ") type=" + audioCard.getType() + ", address=" + audioCard.getAddress()  + ", id=" + audioCard.getName() + "; ");
                    index++;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }
        return microphoneInfos;
    }

    public List<AudioCard> getActiveOutCardIdList() {
        Class audioManager;
        Method method = null;
        List<AudioCard> outputs = null;
        try {
            audioManager = Class.forName("android.media.AudioManager");
            method = audioManager.getDeclaredMethod("getActiveOutAudioCardList");
            outputs = (ArrayList<AudioCard>) method.invoke(mAudioManager);
            int index = 0;
            Log.d(TAG, "----------------------------outputs------------------------");
            for (AudioCard output : outputs) {
                if (output != null) {
//                    selectActiveOutCardIDList.append(output.getAddress()+" "+output.getProductName()+" "+output.getId()+"; ");
                    Log.d(TAG, "outputs: (" + index + ") type="+output.getType()+"address=" + output.getAddress() + ", name=" + output.getName() + "; ");
                    index++;
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }
        return outputs;
    }

    public AudioCard getForceMicroPhone() {
        AudioCard microphoneCard = null;
        try {
            Method method = AudioManager.class.getMethod("getForceMicroPhone");
            microphoneCard = (AudioCard) method.invoke(mAudioManager);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
        Log.d(TAG, "getForceMicroPhone: type=" + microphoneCard.getType() + ", address=" + microphoneCard.getAddress() + ",name=" + microphoneCard.getName());
        return microphoneCard;
    }

    public AudioCard getForceUseOutputDevice() {
        AudioCard speakerCard = null;
        try {
            Method method = AudioManager.class.getMethod("getForceUseOutputDevice");
            speakerCard = (AudioCard) method.invoke(mAudioManager);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
        if (speakerCard != null) {
            Log.d(TAG, "getForceUseOutputDevice: type=" + speakerCard.getType() + ", address=" + speakerCard.getAddress() + ",name=" + speakerCard.getName());
        }
        Log.d(TAG, "getForceUseOutputDevice: speakerCard is null");
        return speakerCard;
    }

    public int setForceUseOutputDevice(AudioCard audioCard) {
        int ret = -1;
        try {
            Method method = AudioManager.class.getMethod("setForceUseOutputDevice", AudioCard.class);
            ret = (int) method.invoke(mAudioManager, audioCard);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Exception", e.toString());
        }
        Log.d(TAG, "setForceUseOutputDevice: ret=" + ret);
        return ret;
    }

}
