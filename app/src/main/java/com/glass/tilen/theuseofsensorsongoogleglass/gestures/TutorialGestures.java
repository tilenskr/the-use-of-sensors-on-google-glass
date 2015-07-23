package com.glass.tilen.theuseofsensorsongoogleglass.gestures;

import android.content.Context;

import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

/**
 * Created by Tilen on 19.7.2015.
 */
public class TutorialGestures extends GestureDetector implements GestureDetector.BaseListener{


    public interface OnGestureCallback
    {
        boolean onGestureDetected(Gesture gesture);
    }
    OnGestureCallback mCallback;

    public TutorialGestures(Context mContext, OnGestureCallback mCallback)
    {
        super(mContext);
        this.mCallback = mCallback;
        this.setBaseListener(this);
    }

    @Override
    public boolean onGesture(Gesture gesture) {
        Global.LogDebug("TutorialGestures.onGesture()" + gesture);
        return mCallback.onGestureDetected(gesture);
    }

}
