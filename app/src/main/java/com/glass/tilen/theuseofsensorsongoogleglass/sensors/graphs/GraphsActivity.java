package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.MultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;

public class GraphsActivity extends MultiLayoutActivity implements MainSensorManager.MainSensorManagerCallback,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private GraphsCardAdapterCommunicator mCommunicator;
    private MainSensorManager mainSensorManager;

    /** for testing purposes**/
    private boolean showValues = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardAdapter = new GraphsCardAdapter(this);
        mCommunicator = (GraphsCardAdapterCommunicator) mCardAdapter.getCommunicator();
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        mCardScroller.setOnItemSelectedListener(this);
        mCardScroller.setOnItemClickListener(this);
        mCardScroller.setOnItemLongClickListener(this);
        mainSensorManager = MainSensorManager.getInstance(this);
        mainSensorManager.setSensorCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_ALL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onSpeechResult(String text) {
        super.onSpeechResult(text);
        if(text.equals(getString(R.string.forward)))
        {
            onItemClick(null, mCardScroller.getSelectedView(), mCardScroller.getSelectedItemPosition(), -1);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("GraphsActivity.onItemSelected(): Position: " + position);
        GraphsCardAdapter.GraphsCard mGraphCard = GraphsCardAdapter.GraphsCard.values()[position];
        mainSensorManager.setSensor(mGraphCard.getSensorType());
        mainSensorManager.registerSensor(14);
        switch (mGraphCard)
        {
            //case ACCELEROMETER:
            case GRAVITY:
                float maxValue = mainSensorManager.getSensorMaxValue();
                maxValue = (float) Math.ceil(maxValue);
                mCommunicator.setChartLimits(view, -maxValue, maxValue);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Global.LogDebug("GraphsActivity.onNothingSelected()");
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onSensorValueChanged(float[] values) {
      /*  switch (mGraphsCard)
       {
            case GYROSCOPE:
                values = Utils.divideWithMaxValue(values, mainSensorManager.getSensorMaxValue(), 8);
                //Global.TestDebug("GraphsActivity.onSensorValueChanged() gyro. or l.acc: values " + Arrays.toString(values));
                break;
            case LINEAR_ACCELERATION:
                values = Utils.divideWithMaxValue(values, mainSensorManager.getSensorMaxValue(), 2.3f);
                break;
            case MAGNETIC_FIELD:
                //values = Utils.normalizeArray(values);
                break;
        }*/
        if(showValues == true) {
            mCommunicator.setAdaptiveChartLimits(mCardScroller.getSelectedView());
            mCommunicator.addNewPoints(mCardScroller.getSelectedView(), values);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAudioManager.playSoundEffect(Sounds.TAP);
        mCommunicator.changeAxis(mCardScroller.getSelectedView());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(Global.isTestingOn()) {
            showValues = !showValues;
            return true;
        }
        return false;
    }
}
