package com.glass.tilen.theuseofsensorsongoogleglass;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

/**
 * Created by Tilen on 9.8.2015.
 */
public abstract class BaseActivity extends Activity {
    protected SpeechRecognition mSpeechRecognition;
    protected AudioManager mAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences.setScreenOn(this);
        mAudioManager = Global.getAudioManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.initializeSpeechRecognizer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSpeechRecognition.shutdownSpeechRecognition();
    }

}