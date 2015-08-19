package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.MultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

public class GraphsActivity extends MultiLayoutActivity implements MainSensorManager.MainSensorManagerCallback,
        AdapterView.OnItemSelectedListener {

    private GraphsCardAdapterCommunicator mCommunicator;
    private MainSensorManager mainSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardAdapter = new GraphsCardAdapter(this);
        mCommunicator = (GraphsCardAdapterCommunicator) mCardAdapter.getCommunicator();
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        mCardScroller.setOnItemSelectedListener(this);
        mainSensorManager = MainSensorManager.getInstance(this);
        mainSensorManager.setSensorCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_LEFT_RIGHT_BACK);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("GraphsActivity.onItemSelected(): Position: " + position);
        mainSensorManager.setSensor(GraphsCardAdapter.GraphsCard.values()[position].getSensorType());
        mainSensorManager.registerSensor();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Global.LogDebug("GraphsActivity.onNothingSelected()");
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onSensorValueChanged(float[] values) {
        mCommunicator.addNewPoints(mCardScroller.getSelectedView(), values);
    }
}
