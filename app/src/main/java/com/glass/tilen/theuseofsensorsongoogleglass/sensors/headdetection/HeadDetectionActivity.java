package com.glass.tilen.theuseofsensorsongoogleglass.sensors.headdetection;

import android.os.Bundle;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;

public class HeadDetectionActivity extends BaseActivity implements SpeechRecognition.SpeechRecognitionCallback {

    private TextView tvFooter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_detection);
        mSpeechRecognition = new SpeechRecognition(this, this, SpeechRecognition.KEYWORD_NAVIGATION_ALL);
        tvFooter = (TextView) findViewById(R.id.tvFooter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        tvFooter.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
