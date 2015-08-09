package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

public class SensorsActivity extends Activity implements AdapterView.OnItemClickListener,
        SpeechRecognition.SpeechRecognitionCallback {

    private CustomCardScrollView mCardScroller;
    private SensorsCardAdapter mCardAdapter;
    private SpeechRecognition mSpeechRecognition;
    private AudioManager mAudioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CustomCardScrollView(this);
        mCardAdapter = new SensorsCardAdapter(this);
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        Preferences.setScreenOn(this);
        mAudioManager = Global.getAudioManager(this);
        mCardScroller.setOnItemClickListener(this);
        mSpeechRecognition = new SpeechRecognition(this, this, SpeechRecognition.KEYWORD_NAVIGATION_ALL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        mCardAdapter.setTextForFooter("");
        mSpeechRecognition.initializeSpeechRecognizer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCardScroller.deactivate();
        mSpeechRecognition.shutdownSpeechRecognition();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("SensorsActivity.onItemClick()");
        mAudioManager.playSoundEffect(Sounds.TAP);
        switch ((SensorsCardAdapter.SensorsCard)mCardAdapter.getItem(position))
        {
            case GRAPH_SENSORS:
                break;
            case AMBIENT_LIGHT:
                break;
            case HEAD_DETECTION:
                break;
            case REPEAT_TUTORIAL:
                break;
            case SENSORS_OVERVIEW:
                break;
            case SETTINGS:
                break;
        }
    }

    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.speak_navigate);
        mCardAdapter.setAnimationForFooterTextView(mCardScroller.getSelectedView());
        setTextForFooter(resultText);
    }

    @Override
    public void onSpeechResult(String text) {
        // can not use switch, because string needs to be declared as final
        // this way is better if we would implement localization (probably never)
        if(text.equals(getString(R.string.forward)))
        {
            onItemClick(null, null, mCardScroller.getSelectedItemPosition(), -1);
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

    private void setTextForFooter(String resultText)
    {
        mCardAdapter.setTextForFooter(resultText);
        for(int i = 0; i < mCardScroller.getChildCount();i++)
        {
            mCardAdapter.setTextForView(mCardScroller.getChildAt(i));
        }
    }
}
