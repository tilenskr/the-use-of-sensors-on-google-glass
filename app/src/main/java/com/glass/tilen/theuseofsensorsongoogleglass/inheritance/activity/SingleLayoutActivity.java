package com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.google.android.glass.media.Sounds;

/**
 * Created by Tilen on 16.8.2015.
 */
public abstract class SingleLayoutActivity extends BaseActivity {
    protected TextView tvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
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
        else if(resultText.equals("-1"))
            resultText = getString(R.string.speak_disabled);
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
