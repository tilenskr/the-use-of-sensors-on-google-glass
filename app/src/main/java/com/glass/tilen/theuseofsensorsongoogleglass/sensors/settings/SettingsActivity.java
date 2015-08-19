package com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.MultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;

import java.util.HashMap;

public class SettingsActivity extends MultiLayoutActivity implements AdapterView.OnItemClickListener {

    private SettingsCardAdapterCommunicator mCommunicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardAdapter = new SettingsCardAdapter(this, getSettingsActions());
        mCommunicator = (SettingsCardAdapterCommunicator) mCardAdapter.getCommunicator();
        mCardScroller.setAdapter(mCardAdapter);
        mCardScroller.setOnItemClickListener(this);
        setContentView(mCardScroller);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_ALL);
    }

    @Override
    public void onSpeechResult(String text) {
        super.onSpeechResult(text);
        if(text.equals(getString(R.string.forward)))
        {
            onItemClick(null, mCardScroller.getSelectedView(), mCardScroller.getSelectedItemPosition(), -1);
        }
    }

    private HashMap<SettingsCardAdapter.SettingsCard,Boolean> getSettingsActions()
    {
        HashMap<SettingsCardAdapter.SettingsCard,Boolean> actions = new HashMap<SettingsCardAdapter.SettingsCard, Boolean>();
        actions.put(SettingsCardAdapter.SettingsCard.SPEECH_RECOGNITION, Preferences.isSpeechRecognitionOn(this));
        return actions;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("SettingsActivity.onItemClick()");
        mAudioManager.playSoundEffect(Sounds.SUCCESS);
        SettingsCardAdapter.SettingsCard mSettingsCard = mCommunicator.getItem(position);
        switch (mSettingsCard)
        {
            case SPEECH_RECOGNITION:
                Preferences.setSpeechRecognition(this);
                String textToDisplay = mSpeechRecognition.setActive();
                mCommunicator.changeActionText(mSettingsCard, view);
                mCommunicator.setFadeOutFadeInAnimationForFooterTextView(view, textToDisplay);
                setTextForFooter(textToDisplay, mCardScroller.getSelectedItemPosition());
                break;
        }

    }
}
