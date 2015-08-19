package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.view.View;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.interface_.BaseCardCommunicator;

/**
 * Created by Tilen on 19.8.2015.
 */
public interface GraphsCardAdapterCommunicator extends BaseCardCommunicator {
    void addNewPoints(View graphsLayout, float[] values);
    void changeAxis(View graphsLayout);
}
