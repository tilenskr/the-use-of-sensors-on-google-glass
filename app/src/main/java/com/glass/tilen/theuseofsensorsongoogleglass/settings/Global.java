package com.glass.tilen.theuseofsensorsongoogleglass.settings;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by Tilen on 19.7.2015.
 */
public class Global {
    private static final String DEBUGTAG = "sensors.debug";
    private static final String INFOTAG = "sensors.info";
    private static final String ERRORTAG = "sensors.error";
    private static final String TESTTAG = "sensors.test";
    private static final String SPEECHTAG = "sensors.speech";
    private static final String SENSORSTAG = "sensors.sensors";



    private static final boolean debug = true;
    private static final boolean info = true;
    private static final boolean error = true;
    private static final boolean test = true;
    private static final boolean speech = true;
    private static final boolean sensors = true;



    public static void LogDebug(String message) {
        if (debug)
            Log.d(DEBUGTAG, message);
    }

    public static void InfoDebug(String message) {
        if (info)
            Log.i(INFOTAG, message);
    }

    public static void ErrorDebug(String message) {
        if (error)
            Log.e(ERRORTAG, message);
    }

    public static void TestDebug(String message) {
        if (test)
            Log.d(TESTTAG, message);
    }

    public static void SpeechDebug(String message) {
        if (speech)
            Log.d(SPEECHTAG, message);
    }

    public static void SensorsDebug(String message) {
        if (sensors)
            Log.d(SENSORSTAG, message);
    }

    public static AudioManager getAudioManager(Context context)
    {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
}
