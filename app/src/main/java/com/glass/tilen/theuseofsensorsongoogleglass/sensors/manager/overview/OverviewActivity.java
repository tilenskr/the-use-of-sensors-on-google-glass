package com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.overview;

import android.os.Bundle;

import com.glass.tilen.theuseofsensorsongoogleglass.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

public class OverviewActivity extends BaseActivity implements SpeechRecognition.SpeechRecognitionCallback {

    private CustomCardScrollView mCardScroller;
    private OverviewCardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CustomCardScrollView(this);
        mCardAdapter = new OverviewCardAdapter(this, MainSensorManager.getAllSensors(this));
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        mSpeechRecognition = SpeechRecognition.getInstance(this, this, SpeechRecognition.KEYWORD_NAVIGATION_ALL);

    }
    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        mCardAdapter.setTextForFooter("");
        mSpeechRecognition.initializeSpeechRecognizer(this, SpeechRecognition.KEYWORD_NAVIGATION_ALL);
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
        mCardAdapter.setAnimationForFooterTextView(mCardScroller.getSelectedView());
        setTextForFooter(resultText);
    }

    @Override
    public void onSpeechResult(String text) {
        // can not use switch, because string needs to be declared as final
        // this way is better if we would implement localization (probably never)
        if(text.equals(getString(R.string.backward)))
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
