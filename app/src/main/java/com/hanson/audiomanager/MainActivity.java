package com.hanson.audiomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioCard;
import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MicrophoneInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hanson.audiomanager.manager.vman.AudioCtrlManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AudioManager mAudioManager = null;
    private static final int AUDIO_TYPE = AudioManager.STREAM_ALARM;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;
    private Spinner microphoneSpinner;
    private Spinner speakerSpinner;
    private final AudioDeviceCallback mAudioDeviceCallback = new AudioDeviceCallback() {
        @Override
        public void onAudioDevicesAdded(AudioDeviceInfo[] addedDevices) {
            super.onAudioDevicesAdded(addedDevices);
            initMicList2();
            initSpeakersList();
        }

        @Override
        public void onAudioDevicesRemoved(AudioDeviceInfo[] removedDevices) {
            super.onAudioDevicesRemoved(removedDevices);
            initMicList2();
            initSpeakersList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        microphoneSpinner = findViewById(R.id.microphoneSpinner);
        speakerSpinner = findViewById(R.id.speakerSpinner);
        if (mAudioManager == null) {
            mAudioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        }

//        try {
//            TestReflect.main("android.media.AudioManager");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        AudioUtilNew.getInstance(this).getDeviceVolumeBehavior();
//        AudioUtilNew.getInstance(this).setDeviceVolumeBehavior();
//        AudioUtilNew.getInstance(this).getDeviceVolumeBehavior();
//        for (int i = 0; i <=5; i++) {
//            Log.d(TAG, "onCreate: i="+i+", getStreamVolume="+mAudioManager.getStreamVolume(i));
//        }
//        Log.d(TAG, "onCreate: isMusicActive="+mAudioManager.isMusicActive());
//        getAudioFocus();
//        setMinVolume();

//        AudioUtil.getInstance(this).getAudioInCardList();
//        AudioUtil.getInstance(this).getAudioInCardId();
//        AudioUtilNew.getInstance(this).getForceMicroPhone();
//        AudioUtilNew.getInstance(this).getForceMicroPhone();
//        AudioUtilNew.getInstance(this).getForceUseOutputDevice();
//        List<AudioCard> outputs =  AudioUtilNew.getInstance(this).getActiveOutCardIdList();
//        List<AudioCard> inputs =  AudioUtilNew.getInstance(this).getAudioInCardList();
//        Log.d(TAG, "onCreate: outputs.get(3)="+outputs.get(2).getProductName());
//        AudioUtil.getInstance(this).setForceUseOutputDevice(outputs.get(2));
//        Intent intentAction = new Intent(this,MainService.class);
//        this.startForegroundService(intentAction);
        Log.d(TAG,"startActivity = MainService");


        initMicList2();
        initSpeakersList();


//        AudioDeviceInfo[] outputs =  AudioUtil.getInstance(this).getDevices(AudioManager.GET_DEVICES_OUTPUTS);
//        List<MicrophoneInfo> inputs =  AudioUtil.getInstance(this).getMicrophones();

//        AudioUtil.getInstance(this).setForceMicroPhone(inputs.get(1));
        AudioUtil.getInstance(this).getForceMicroPhone();

        AudioUtil.getInstance(this).getForceUseOutputDevice();
//        AudioUtil.getInstance(this).setForceUseOutputDevice(outputs[1]);

//        mAudioManager.registerAudioDeviceCallback(mAudioDeviceCallback, new Handler());

//        AudioCtrlManager.getInstance(this).getAudioAmpMute();
//        AudioCtrlManager.getInstance(this).getHeadphoneMute();
//        AudioCtrlManager.getInstance(this).setHeadphoneMute(1);
//        AudioCtrlManager.getInstance(this).getHeadphoneMute();

        getDisplayDeviceStatic(AudioUtil.getInstance(this).getDevices(AudioManager.GET_DEVICES_OUTPUTS));
        getDisplayDeviceStatic(AudioUtil.getInstance(this).getDevices(AudioManager.GET_DEVICES_INPUTS));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
//        setVolume(20);
        mAudioManager.abandonAudioFocus(afChangeListener);
        mAudioManager.unregisterAudioDeviceCallback(mAudioDeviceCallback);
    }

    private void initMicList() {
        List<MicrophoneInfo> inputs =  AudioUtil.getInstance(this).getMicrophones();
        List<String> microphoneList = new ArrayList<String>();
        for (MicrophoneInfo microphoneInfo : inputs) {
            String name = "";
            switch (microphoneInfo.getType()) {
                case AudioDeviceInfo.TYPE_BUILTIN_MIC:
                    name = "BUILTIN_MIC";
                    break;
                case AudioDeviceInfo.TYPE_HDMI: {
                    name = "HDMI In";
                    break;
                }
                case AudioDeviceInfo.TYPE_REMOTE_SUBMIX: {
                    name = "REMOTE SUBMIX";
                    break;
                }
                default: {
                    name = microphoneInfo.getDescription();
                    break;
                }
            }
            microphoneList.add(name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,microphoneList);
        microphoneSpinner.setAdapter(adapter);
    }

    private void initMicList2() {
        AudioDeviceInfo[] inputs =  getDisplayDeviceStatic(AudioUtil.getInstance(this).getDevices(AudioManager.GET_DEVICES_INPUTS);
        List<String> microphoneList = new ArrayList<String>();
        for (AudioDeviceInfo microphoneInfo : inputs) {
            String name = "";
            switch (microphoneInfo.getType()) {
                case AudioDeviceInfo.TYPE_BUILTIN_MIC:
                    name = "BUILTIN_MIC";
                    break;
                case AudioDeviceInfo.TYPE_HDMI: {
                    name = "HDMI In";
                    break;
                }
                case AudioDeviceInfo.TYPE_REMOTE_SUBMIX: {
                    name = "REMOTE SUBMIX";
                    break;
                }
                default: {
                    name = (String) microphoneInfo.getProductName();
                    break;
                }
            }
            microphoneList.add(name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,microphoneList);
        microphoneSpinner.setAdapter(adapter);
    }

    private void initSpeakersList() {
        AudioDeviceInfo[] outputs =  getDisplayDeviceStatic(AudioUtil.getInstance(this).getDevices(AudioManager.GET_DEVICES_OUTPUTS));
        List<String> speakersList = new ArrayList<String>();

        for (AudioDeviceInfo device : outputs) {
            String name = "";
            switch (device.getType()) {
                case AudioDeviceInfo.TYPE_BUILTIN_SPEAKER:
                    name = "BUILTIN Speaker";
                    break;
                default: {
                    name = (String) device.getProductName();
                    break;
                }
            }
            speakersList.add(name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,speakersList);
        speakerSpinner.setAdapter(adapter);
    }

    private AudioDeviceInfo[] getDisplayDeviceStatic(AudioDeviceInfo[] audioDeviceArray) {
        boolean isDebugDisplayDeviceList = true;
        boolean isEnableCvteAudioProcess = true;
        ArrayList<AudioDeviceInfo> displayDeviceList = new ArrayList<AudioDeviceInfo>(Arrays.asList(audioDeviceArray));
        for (AudioDeviceInfo device : audioDeviceArray) {
            if (isEnableCvteAudioProcess) {
                String keyStr = "card=";
                int index = device.getAddress().indexOf(keyStr);
                int cardId = 0;
                if (index >= 0 && device.getAddress().length() > keyStr.length()) {
                    cardId = Integer.parseInt(device.getAddress().substring(index + keyStr.length(), index + index + keyStr.length() +1));
                }
                if ( cardId == 6) {
                    Log.d(TAG, "getDisplayDeviceStatic:  remove device:name="+device.getProductName()+", card id="+cardId);
                    displayDeviceList.remove(device);
                }
            } else if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
                displayDeviceList.remove(device);
            }
        }

        if (isDebugDisplayDeviceList) {
            for (int i = 0; i < displayDeviceList.size(); i++) {
                Log.d(TAG, "getDisplayDeviceStatic: i="+i
                        +" name="+displayDeviceList.get(i).getProductName()
                        +", address="+displayDeviceList.get(i).getAddress()
                        +", address="+displayDeviceList.get(i).getType());
            }
        }
        return displayDeviceList.toArray(new AudioDeviceInfo[displayDeviceList.size()]);
    }

    private void setMinVolume(){
        Log.d(TAG, "[setMinVolume]: AUDIO_TYPE="+AUDIO_TYPE+", getStreamMinVolume="+mAudioManager.getStreamMinVolume(AUDIO_TYPE)
                +", getStreamMaxVolume="+mAudioManager.getStreamMaxVolume(AUDIO_TYPE));
        mAudioManager.setStreamVolume(AUDIO_TYPE,mAudioManager.getStreamMinVolume(AUDIO_TYPE), AudioManager.FLAG_PLAY_SOUND);
    }

    private void setMaxVolume(){
        Log.d(TAG, "[setMaxVolume]: AUDIO_TYPE="+AUDIO_TYPE+", getStreamMinVolume="+mAudioManager.getStreamMinVolume(AUDIO_TYPE)
                +", getStreamMaxVolume="+mAudioManager.getStreamMaxVolume(AUDIO_TYPE));
        mAudioManager.setStreamVolume(AUDIO_TYPE,mAudioManager.getStreamMinVolume(AUDIO_TYPE), AudioManager.FLAG_PLAY_SOUND);
    }

    private void setVolume(int volume){
        Log.d(TAG, "[setVolume]: AUDIO_TYPE="+AUDIO_TYPE+", volume="+volume);
        mAudioManager.setStreamVolume(AUDIO_TYPE,volume, AudioManager.FLAG_PLAY_SOUND);
    }

    private void getAudioFocus(){
        afChangeListener = focusChange -> {
            Log.d(TAG, "onAudioFocusChange: focusChange="+focusChange);
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus
                // Pause playback immediately
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Lower the volume, keep playing
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
            }
        };
        // Request audio focus for playback
        int result = mAudioManager.requestAudioFocus(afChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback
            Log.d(TAG, "getAudioFocus: Start playback");
        }

        // 当播放完成时，建议调用abandonAudioFocus()方法来释放音频焦点，
        // 通知系统当前App不再需要音频焦点，解除OnAudioFocusChangeListener的注册。
        // Abandon audio focus when playback complete
        
    }
}