package com.hanson.audiomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioCard;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MicrophoneInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AudioManager mAudioManager = null;
    private static final int AUDIO_TYPE = AudioManager.STREAM_ALARM;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;
    private Spinner microphoneSpinner;
    private Spinner speakerSpinner;

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

        for (int i = 0; i <=5; i++) {
            Log.d(TAG, "onCreate: i="+i+", getStreamVolume="+mAudioManager.getStreamVolume(i));
        }
        Log.d(TAG, "onCreate: isMusicActive="+mAudioManager.isMusicActive());
//        getAudioFocus();
//        setMinVolume();

//        AudioUtil.getInstance(this).getAudioInCardList();
//        AudioUtil.getInstance(this).getActiveOutCardIdList();
//        AudioUtil.getInstance(this).getAudioInCardId();
//        AudioUtilNew.getInstance(this).getForceMicroPhone();
//        AudioUtilNew.getInstance(this).getForceUseOutputDevice();
//        List<AudioDeviceInfo> outputs =  AudioUtil.getInstance(this).getActiveOutCardIdList_new();
//        List<MicrophoneInfo> inputs =  AudioUtil.getInstance(this).getAudioInCardList_new();
//        Log.d(TAG, "onCreate: outputs.get(3)="+outputs.get(2).getProductName());
//        AudioUtil.getInstance(this).setForceUseOutputDevice(outputs.get(2));
//        Intent intentAction = new Intent(this,MainService.class);
//        this.startForegroundService(intentAction);
        Log.d(TAG,"startActivity = MainService");


        initMicList();
        initSpeakersList();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
//        setVolume(20);
        mAudioManager.abandonAudioFocus(afChangeListener);
    }

    private void initMicList() {
        List<AudioCard> inputs =  AudioUtilNew.getInstance(this).getAudioInCardList();
        List<String> microphoneList = new ArrayList<String>();
        for (AudioCard microphoneInfo : inputs) {
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
                    name = microphoneInfo.getName();
                    break;
                }
            }
            microphoneList.add(name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,microphoneList);
        microphoneSpinner.setAdapter(adapter);
    }

    private void initSpeakersList() {
        List<AudioCard> outputs =  AudioUtilNew.getInstance(this).getActiveOutCardIdList();
        List<String> speakersList = new ArrayList<String>();
        for (AudioCard output : outputs) {
            String name = "";
            switch (output.getType()) {
                case AudioDeviceInfo.TYPE_BUILTIN_SPEAKER:
                    name = "BUILTIN Speaker";
                    break;
                default: {
                    name = output.getName();
                    break;
                }
            }
            speakersList.add(name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,speakersList);
        speakerSpinner.setAdapter(adapter);
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