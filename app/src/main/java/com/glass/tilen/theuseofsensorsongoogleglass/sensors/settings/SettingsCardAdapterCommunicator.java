package com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings;

import android.view.View;

import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.interface_.BaseCardCommunicator;

/**
 * Created by Tilen on 17.8.2015.
 */
public interface SettingsCardAdapterCommunicator extends BaseCardCommunicator{
    SettingsCardAdapter.SettingsCard getItem(int i);
    void changeActionText (SettingsCardAdapter.SettingsCard mSettingsCard, View view);
    void setFadeOutFadeInAnimationForFooterTextView(View sensorsLayout, String text);
}
