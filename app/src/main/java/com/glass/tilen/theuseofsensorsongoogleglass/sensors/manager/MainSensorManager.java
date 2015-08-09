package com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by Tilen on 9.8.2015.
 */
public class MainSensorManager {
    private Context mContext;
    private static SensorManager mSensorManager = null;

    private static MainSensorManager ourInstance = null;

    public static MainSensorManager getInstance(Context mContext) {
        if(ourInstance == null)
        {
            ourInstance = new MainSensorManager(mContext);
        }
        return ourInstance;
    }

    private MainSensorManager(Context mContext) {
        this.mContext = mContext;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
    }


    public static List<Sensor> getAllSensors(Context mContext)
    {
        // just to be sure if you call getAllSensors, before class is initialized
        if(mSensorManager == null)
            mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        return mSensorManager.getSensorList(Sensor.TYPE_ALL);
    }
}
