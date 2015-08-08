package com.glass.tilen.theuseofsensorsongoogleglass;

import android.app.Application;

import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

/**
 * Created by Tilen on 8.8.2015.
 */
public class MainApplication extends Application {
    private static SpeechRecognition mSpeechRecognition;

    @Override
    public void onCreate() {
        super.onCreate();
        mSpeechRecognition = SpeechRecognition.getInstance(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static SpeechRecognition getInstance()
    {
        return mSpeechRecognition;
    }
}
