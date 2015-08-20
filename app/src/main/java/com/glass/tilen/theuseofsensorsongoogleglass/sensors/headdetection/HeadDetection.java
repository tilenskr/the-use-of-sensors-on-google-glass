package com.glass.tilen.theuseofsensorsongoogleglass.sensors.headdetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.google.android.glass.content.Intents;

/**
 * Created by Tilen on 14.8.2015.
 */
public class HeadDetection {
    private Context mContext;
    private HeadDetectionCallback mCallback;

    public interface HeadDetectionCallback
    {
        void onHeadDetectionOn();
        void onHeadDetectionOff();
    }

    public HeadDetection (Context mContext, HeadDetectionCallback mCallback)
    {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    /** will listen to ACTION_ON_HEAD_STATE_CHANGED_EVENTS **/
    private final BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (Intents.ACTION_ON_HEAD_STATE_CHANGED.equals(intent.getAction())) {
                boolean onHead = intent.getBooleanExtra(Intents.EXTRA_IS_ON_HEAD,
                        false);
                Global.LogDebug("HeadDetection.broadcastReceiver.onReceive(): OnHeadChange: " + onHead);
                if (onHead)
                    mCallback.onHeadDetectionOn();
                else
                    mCallback.onHeadDetectionOff();
            }
        }
    };

    public void register()
    {
        mContext.registerReceiver(broadCastReceiver, new IntentFilter(
                Intents.ACTION_ON_HEAD_STATE_CHANGED));
    }
    public void unregister()
    {
        mContext.unregisterReceiver(broadCastReceiver);
    }
}
