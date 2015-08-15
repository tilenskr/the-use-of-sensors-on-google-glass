package com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

import java.util.HashMap;

public class SettingsActivity extends BaseActivity  implements SpeechRecognition.SpeechRecognitionCallback, AdapterView.OnItemClickListener {

    private CardScrollView mCardScroller;
    private SettingsCardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CardScrollView(this);
        mCardAdapter = new SettingsCardAdapter(this, getSettingsActions());
        mCardScroller.setAdapter(mCardAdapter);
        mCardScroller.setOnItemClickListener(this);
        setContentView(mCardScroller);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_ALL);
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        mCardAdapter.setTextForFooter("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCardScroller.deactivate();
    }


    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.speak_navigate);
        mCardAdapter.setAnimationForFooterTextView(mCardScroller.getSelectedView(), resultText);
        setTextForFooter(resultText, mCardScroller.getSelectedItemPosition());
    }

    @Override
    public void onSpeechResult(String text) {
        // can not use switch, because string needs to be declared as final
        // this way is better if we would implement localization (probably never)
        if(text.equals(getString(R.string.forward)))
        {
            onItemClick(null, mCardScroller.getSelectedView(), mCardScroller.getSelectedItemPosition(), -1);
        }
        else if(text.equals(getString(R.string.backward)))
        {
            mAudioManager.playSoundEffect(Sounds.DISMISSED);
            finish();
        }
        else if(text.equals(getString(R.string.left)))
        {
            int position = mCardScroller.getSelectedItemPosition();
            position--;
            navigateToCard(position);
        }
        else if(text.equals(getString(R.string.right)))
        {
            int position = mCardScroller.getSelectedItemPosition();
            position++;
            navigateToCard(position);
        }

    }

    /** Navigates to card at given position. */
    private void navigateToCard(int position) {
        if(position >= 0 && position < mCardAdapter.getCount())
            mCardScroller.animate(position, CardScrollView.Animation.NAVIGATION);
    }

    private void setTextForFooter(String resultText, int position)
    {
        mCardAdapter.setTextForFooter(resultText);
        for(int i = 0; i < mCardScroller.getChildCount();i++)
        {
            if(i == position)
                continue;
            mCardAdapter.setTextForView(mCardScroller.getChildAt(i));
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
        SettingsCardAdapter.SettingsCard mSettingsCard = mCardAdapter.getItem(position);
        switch (mSettingsCard)
        {
            case SPEECH_RECOGNITION:
                Preferences.setSpeechRecognition(this);
                String textToDisplay = mSpeechRecognition.setActive();
                mCardAdapter.changeActionText(mSettingsCard, view);
                mCardAdapter.setFadeOutFadeInAnimationForFooterTextView(view, textToDisplay);
                setTextForFooter(textToDisplay, mCardScroller.getSelectedItemPosition());
                break;
        }

    }
}
