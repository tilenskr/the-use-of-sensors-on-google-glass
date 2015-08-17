package com.glass.tilen.theuseofsensorsongoogleglass.sensors.headdetection;

import android.os.Bundle;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.SingleLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.SoundPlayer;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

public class HeadDetectionActivity extends SingleLayoutActivity implements
        HeadDetection.HeadDetectionCallback {

    private HeadDetection mHeadDetection;
    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_detection);
        tvFooter = (TextView) findViewById(R.id.tvFooter);
        mSoundPlayer = new SoundPlayer(this);
        mSoundPlayer.setSounds(R.raw.night_rain, R.raw.crickets);
        mHeadDetection = new HeadDetection(this, this);
        mHeadDetection.register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_BACK);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeadDetection.unregister();
        mSoundPlayer.stop();
    }

    @Override
    public void onHeadDetectionOn() {
        mSoundPlayer.play(R.raw.night_rain, true);
    }

    @Override
    public void onHeadDetectionOff() {
        mSoundPlayer.play(R.raw.crickets, true);
    }
}
