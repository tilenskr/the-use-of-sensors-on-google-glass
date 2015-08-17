package com.glass.tilen.theuseofsensorsongoogleglass.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.checkmark.CheckMarkView;
import com.glass.tilen.theuseofsensorsongoogleglass.gestures.TutorialGestures;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.BaseMultiLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.SensorsActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.widget.CardScrollView;

public class TutorialActivity extends BaseMultiLayoutActivity implements TutorialGestures.OnGestureCallback,
        AdapterView.OnItemClickListener {

    private TutorialCardAdapterCommunicator mCommunicator;
    private TutorialGestures mGestureDetector;
    private Handler mHandler;

    // constants from bundle
    /**
     * we need to start activity if we launch it from beginning, otherwise we don't
     * (SensorsActivity is on backstack)
     **/
    public final static String START_ACTIVITY = "start_activity";
    private boolean startActivity;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Global.LogDebug("TutorialActivity.onCreate()");
        startActivity = getIntent().getBooleanExtra(START_ACTIVITY, true);
        mCardAdapter = new TutorialCardAdapter(this);
        mCommunicator = (TutorialCardAdapterCommunicator) mCardAdapter.getCommunicator();
        mCommunicator.insertCardWithoutAnimation(TutorialCardAdapter.TutorialCard.TAP_TOUCHPAD);
        mCardScroller.setAdapter(mCardAdapter);
        TutorialCardAdapter.TutorialCard.initializeEnums();
        setContentView(mCardScroller);
        mCardScroller.setOnItemClickListener(this);
        mGestureDetector = new TutorialGestures(this, this);
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_VERTICAL);
    }

    /**
     * called first (before we used (recommended) onGenericMotionEvent())
     **/
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mGestureDetector.onMotionEvent(ev);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("TutorialActivity.onItemClick()");
        TutorialCardAdapter.TutorialCard mTutorialCard =  mCommunicator.getItem(position);
        if (mTutorialCard.getHasBeenDone()) return;
        switch (mTutorialCard) {
            case TAP_TOUCHPAD:
                mTutorialCard.setHasBeenDone(true);
                CheckMarkView cvMarkView = (CheckMarkView) view.findViewById(R.id.cvCheckMark);
                cvMarkView.toggle();
                // cvMarkView.setChecked(true);
                proceedWithNextCard(TutorialCardAdapter.TutorialCard.SWIPEDOWN);
                break;
            case LAST:
                goToSensorsActivity();
                break;
            default:
                mAudioManager.playSoundEffect(Sounds.ERROR);
                break;
        }
    }

    /**
     * insert at end
     **/
    public void insertCardWithAnimation(TutorialCardAdapter.TutorialCard mTutorialCard) {
        mCommunicator.insertCardWithoutAnimation(mTutorialCard);
        //mCardAdapter.notifyDataSetChanged();
        mCardScroller.animate(mCardAdapter.getCount() - 1, CardScrollView.Animation.INSERTION);
    }

    @Override
    public boolean onGestureDetected(Gesture gesture) {
        TutorialCardAdapter.TutorialCard mTutorialCard = (TutorialCardAdapter.TutorialCard) mCardScroller.getSelectedItem();
        boolean returnValue = false;
        /** we don't want to get any event if there are ticks **/
        if (mTutorialCard.getHasBeenDone()) {
            return false;
        }
        switch (mTutorialCard) {
            case SWIPING:
                returnValue = checkForCorrectGesture(mTutorialCard, TutorialCardAdapter.TutorialCard.SAYLEFTRIGHT, gesture, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT);
                mSpeechRecognition.switchSearch(SpeechRecognition.KEYWORD_HORIZONTAL);
                break;
            case SWIPEDOWN:
               returnValue = checkForCorrectGesture(mTutorialCard,  TutorialCardAdapter.TutorialCard.SAYUPDOWN, gesture, Gesture.SWIPE_DOWN);
                break;
        }
        return returnValue;
    }

    private boolean checkForCorrectGesture(TutorialCardAdapter.TutorialCard mTutorialCard, TutorialCardAdapter.TutorialCard nextCard, Gesture currentGesture, Gesture... correctGestures) {
        boolean returnValue = false;
        int id = -1;
        if (currentGesture == correctGestures[0]) {
            id = R.id.cvCheckMark;
        } else if (correctGestures.length > 1 && currentGesture == correctGestures[1]) {
            id = R.id.cvCheckMark2;
        }
        if (id != -1) {
            returnValue = true;
            setCheckMarkAndProceed(mTutorialCard, nextCard, id);
        }
        return returnValue;
    }


    private void setCheckMarkAndProceed(TutorialCardAdapter.TutorialCard currentCard, TutorialCardAdapter.TutorialCard nextCard, int id) {
        View tutorialLayout = mCardScroller.getSelectedView();
        CheckMarkView cvMarkView = (CheckMarkView) tutorialLayout.findViewById(id);
        if (!cvMarkView.isChecked()) {
            cvMarkView.toggle();
            currentCard.setCheckMarkPressed();
        }
        Global.LogDebug("TutorialActivity.setCheckMarkAndProceed():  " + currentCard + ".getCheckMarkPressed(): " + currentCard.getCheckMarkPressed());
        if (currentCard.getCheckMarkPressed() == currentCard.getCheckMarkCount()) {
            currentCard.setHasBeenDone(true);
            proceedWithNextCard(nextCard);
        }
    }

    private void proceedWithNextCard(final TutorialCardAdapter.TutorialCard mTutorialCard) {
        Global.LogDebug("TutorialActivity.proceedWithNextCard() " + mTutorialCard);
        mAudioManager.playSoundEffect(Sounds.SUCCESS);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertCardWithAnimation(mTutorialCard);
            }
        }, CheckMarkView.CHECK_MARK_ANIMATION_DURATION);

    }

    @Override
    public void onSpeechResult(String text) {
        if (text.equals("skip tutorial")) {
            goToSensorsActivity();
            return;
        }
        TutorialCardAdapter.TutorialCard mTutorialCard = (TutorialCardAdapter.TutorialCard) mCardScroller.getSelectedItem();
        /** we don't want to get any event if there are ticks **/
        if (mTutorialCard.getHasBeenDone()) {
            return;
        }
        switch (mTutorialCard) {
            case SAYUPDOWN:
                checkForCorrectSpeech(mTutorialCard, TutorialCardAdapter.TutorialCard.SWIPING ,text, "forward", "backward");
                break;
            case SAYLEFTRIGHT:
                checkForCorrectSpeech(mTutorialCard, TutorialCardAdapter.TutorialCard.LAST ,text, "left", "right");
                break;
        }
    }

    private void checkForCorrectSpeech(TutorialCardAdapter.TutorialCard mTutorialCard, TutorialCardAdapter.TutorialCard nextCard, String spokenText, String... correctTexts) {
            int id = -1;
            if (spokenText.equals(correctTexts[0])) {
                id = R.id.cvCheckMark;
            } else if (correctTexts.length > 1 &&spokenText.equals(correctTexts[1])) {
                id = R.id.cvCheckMark2;
            }
            if (id != -1) {
                setCheckMarkAndProceed(mTutorialCard, nextCard, id);
            }
    }

    private void goToSensorsActivity()
    {
        mAudioManager.playSoundEffect(Sounds.SUCCESS);
        Preferences.setStartActivity(this);
        if(startActivity == true) {
            Intent intent = new Intent(this, SensorsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
