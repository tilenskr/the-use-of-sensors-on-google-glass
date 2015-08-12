package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.overview.OverviewActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings.SettingsActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.glass.tilen.theuseofsensorsongoogleglass.tutorial.TutorialActivity;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

public class SensorsActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        SpeechRecognition.SpeechRecognitionCallback {

    private CustomCardScrollView mCardScroller;
    private SensorsCardAdapter mCardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CustomCardScrollView(this);
        mCardAdapter = new SensorsCardAdapter(this);
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCardScroller.deactivate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("SensorsActivity.onItemClick()");
        mAudioManager.playSoundEffect(Sounds.TAP);
        Intent intent = null;
        switch ((SensorsCardAdapter.SensorsCard)mCardAdapter.getItem(position))
        {
            case GRAPH_SENSORS:
                break;
            case AMBIENT_LIGHT:
                break;
            case HEAD_DETECTION:
                break;
            case REPEAT_TUTORIAL:
                Preferences.setSpeechRecognitionState(this, true);
                intent = new Intent(this, TutorialActivity.class);
                intent.putExtra(TutorialActivity.START_ACTIVITY, false);
                break;
            case SENSORS_OVERVIEW:
                intent = new Intent(this, OverviewActivity.class);
                break;
            case SETTINGS:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        startActivity(intent);

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
