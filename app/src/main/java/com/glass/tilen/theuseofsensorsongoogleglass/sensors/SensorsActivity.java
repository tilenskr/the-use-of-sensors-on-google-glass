package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

public class SensorsActivity extends Activity implements AdapterView.OnItemClickListener,
        SpeechRecognition.SpeechRecognitionCallback {

    private CustomCardScrollView mCardScroller;
    private SensorsCardAdapter mCardAdapter;
    private SpeechRecognition mSpeechRecognition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CustomCardScrollView(this);
        mCardAdapter = new SensorsCardAdapter(this);
        mCardScroller.setAdapter(mCardAdapter);
        setContentView(mCardScroller);
        Preferences.setScreenOn(this);
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

    }

    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.say_skip_tutorial);
        mCardAdapter.setAnimationForFooterTextView(mCardScroller.getSelectedView());
        setTextForFooter(resultText);
    }

    @Override
    public void onSpeechResult(String text) {

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
