package com.glass.tilen.theuseofsensorsongoogleglass.sensors.overview;

import android.os.Bundle;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.MultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

public class OverviewActivity extends MultiLayoutActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardAdapter = new OverviewCardAdapter(this, MainSensorManager.getAllSensors(this));
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_LEFT_RIGHT_BACK);
    }
}
