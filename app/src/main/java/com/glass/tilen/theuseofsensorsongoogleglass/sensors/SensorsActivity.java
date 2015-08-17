package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.MultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.headdetection.HeadDetectionActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.light.AmbientLightActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.overview.OverviewActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings.SettingsActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.glass.tilen.theuseofsensorsongoogleglass.tutorial.TutorialActivity;
import com.google.android.glass.media.Sounds;

public class SensorsActivity extends MultiLayoutActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardAdapter = new SensorsCardAdapter(this);
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        mCardScroller.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_ALL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("SensorsActivity.onItemClick()");
        mAudioManager.playSoundEffect(Sounds.TAP);
        Intent intent = null;
        switch ((SensorsCardAdapter.SensorsCard)mCardAdapter.getItem(position))
        {
            case GRAPH_SENSORS:
                break;
            case AMBIENT_LIGHT:
                intent = new Intent(this, AmbientLightActivity.class);
                break;
            case HEAD_DETECTION:
                intent = new Intent(this, HeadDetectionActivity.class);
                break;
            case REPEAT_TUTORIAL:
                Preferences.setSpeechRecognitionState(this, true);
                intent = new Intent(this, TutorialActivity.class);
                intent.putExtra(TutorialActivity.START_ACTIVITY, false);
                break;
            case SENSORS_OVERVIEW:
                intent = new Intent(this, OverviewActivity.class);
                break;
            case SETTINGS:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onSpeechResult(String text) {
        super.onSpeechResult(text);
        if(text.equals(getString(R.string.forward)))
        {
            onItemClick(null, mCardScroller.getSelectedView(), mCardScroller.getSelectedItemPosition(), -1);
        }
    }
}
