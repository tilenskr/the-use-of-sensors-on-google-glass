package com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.Utils;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tilen on 9.8.2015.
 */
public class MainSensorManager implements SensorEventListener {
    private static SensorManager mSensorManager = null;
    private static MainSensorManager ourInstance = null;
    private Sensor mSensors;
    private MainSensorManagerCallback mCallback;
    /**
        how often do we take samples
     */
    private int samplesFrequency;
    private int sensorSampleCounter;
    /** for low-pass filter **/
    private float[] previousValues;
    public interface MainSensorManagerCallback
    {
        void onSensorValueChanged(float[] values);
    }

    public static MainSensorManager getInstance(Context mContext) {
        if(ourInstance == null)
        {
            ourInstance = new MainSensorManager(mContext);
        }
        return ourInstance;
    }

    private MainSensorManager(Context mContext) {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }

    public void setSensorCallback(MainSensorManagerCallback mCallback)
    {
        this.mCallback = mCallback;
    }

    public void setSensor(int sensorType)
    {
        mSensors = mSensorManager.getDefaultSensor(sensorType);
        Global.SensorsDebug("MainSensorManager.setSensor(): Sensor name: " + mSensors.getName() +
                " Maximum sensor value: " + mSensors.getMaximumRange());
    }

    /**
     *
     * @param samplesFrequency - how often do we take samples
     */
    public void registerSensor(int samplesFrequency)
    {
        this.samplesFrequency = samplesFrequency;
        sensorSampleCounter = 0;
        previousValues = null;
        mSensorManager.unregisterListener(this);
        mSensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_UI);
    }

    public void unRegisterSensor()
    {
        mSensorManager.unregisterListener(this);
    }
    public static List<Sensor> getAllSensors(Context mContext)
    {
        // just to be sure if you call getAllSensors, before class is initialized
        if(mSensorManager == null)
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        return mSensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorSampleCounter++;
        sensorSampleCounter %= samplesFrequency;
        if(sensorSampleCounter == 0) {
            Global.SensorsDebug("MainSensorManager.onSensorChanged(): values " + Arrays.toString(event.values));
            float[] newValues = Utils.lowPass(previousValues, event.values);
            previousValues = event.values;
            mCallback.onSensorValueChanged(newValues);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Global.SensorsDebug("MainSensorManager.onAccuracyChanged(): accuracy: " + accuracy);
    }

    public float getSensorMaxValue()
    {
       return mSensors.getMaximumRange();
    }
}
