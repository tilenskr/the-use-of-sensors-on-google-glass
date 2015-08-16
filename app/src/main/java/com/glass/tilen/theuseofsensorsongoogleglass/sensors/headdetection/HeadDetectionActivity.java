package com.glass.tilen.theuseofsensorsongoogleglass.sensors.headdetection;

import android.os.Bundle;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.SoundPlayer;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;

public class HeadDetectionActivity extends BaseActivity implements SpeechRecognition.SpeechRecognitionCallback,
        HeadDetection.HeadDetectionCallback {

    private TextView tvFooter;
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
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_ALL);
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        tvFooter.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeadDetection.unregister();
        mSoundPlayer.stop();
    }

    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.speak_navigate);
        FrequentAnimations.fadeIn(tvFooter, resultText);
    }

    @Override
    public void onSpeechResult(String text) {
        if(text.equals(getString(R.string.backward)))
        {
            mAudioManager.playSoundEffect(Sounds.DISMISSED);
            finish();
        }
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
