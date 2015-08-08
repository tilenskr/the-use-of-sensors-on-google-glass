package com.glass.tilen.theuseofsensorsongoogleglass.tutorial;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.checkmark.CheckMarkView;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.gestures.TutorialGestures;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.widget.CardScrollView;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class TutorialActivity extends Activity implements TutorialGestures.OnGestureCallback,
        AdapterView.OnItemClickListener, SpeechRecognition.SpeechRecognitionCallback {
    private CustomCardScrollView mCardScroller;
    private TutorialCardAdapter mCardAdapter;
    private TutorialGestures mGestureDetector;
    private AudioManager mAudioManager;
    private Handler mHandler;
    private SpeechRecognition mSpeechRecognition;

    /**
     * keyword constants for SpeechRecognition
     **/
    private final static String KEYWORD_VERTICAL = "tutorial_up_down";
    private final static String KEYWORD_HORIZONTAL = "tutorial_left_right";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Global.LogDebug("TutorialActivity.onCreate()");
        mCardScroller = new CustomCardScrollView(this);
        mCardAdapter = new TutorialCardAdapter(this);
        mCardAdapter.insertCardWithoutAnimation(TutorialCardAdapter.TutorialCard.TAP_TOUCHPAD);
        mCardScroller.setAdapter(mCardAdapter);
        initializeEnums();
        setContentView(mCardScroller);
        Preferences.setScreenOn(this);
        mAudioManager = Global.getAudioManager(this);
        mCardScroller.setOnItemClickListener(this);
        mGestureDetector = new TutorialGestures(this, this);
        mHandler = new Handler();
        mSpeechRecognition = new SpeechRecognition(this, this, KEYWORD_VERTICAL, KEYWORD_HORIZONTAL);
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
        TutorialCardAdapter.TutorialCard mTutorialCard = mCardAdapter.getTutorialCardAt(position);
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
                goToDifferentActivity();
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
        mCardAdapter.insertCardWithoutAnimation(mTutorialCard);
        //mCardAdapter.notifyDataSetChanged();
        mCardScroller.animate(mCardAdapter.getCount() - 1, CardScrollView.Animation.INSERTION);
    }

    /**
     * need to do this, otherwise variable values will retain the same regardless of activity lifecycle
     **/
    private void initializeEnums() {
        for (TutorialCardAdapter.TutorialCard mTutorialCard : TutorialCardAdapter.TutorialCard.values()) {
            mTutorialCard.setHasBeenDone(false);
            mTutorialCard.clearCheckMarkPressed();
        }
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
                returnValue = checkForCorrectGesture(mTutorialCard,  TutorialCardAdapter.TutorialCard.SAYLEFTRIGHT, gesture, Gesture.SWIPE_LEFT, Gesture.SWIPE_RIGHT); //TODO set proper card
                mSpeechRecognition.switchSearch(KEYWORD_HORIZONTAL);
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
            goToDifferentActivity();
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

    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.say_skip_tutorial);
        setAnimationForFooterTextView();
        mCardAdapter.setTextForFooter(resultText);
    }

    private void setAnimationForFooterTextView()
    {
        View tutorialLayout = mCardScroller.getSelectedView();
        View tvFooter = tutorialLayout.findViewById(R.id.tvFooter);
        // we use the same duration as animation for CheckMarkView
        FrequentAnimations.fadeIn(tvFooter, CheckMarkView.CHECK_MARK_ANIMATION_DURATION);
    }

    private void goToDifferentActivity() //TODO change name to proper activity na set proper activity
    {
        mAudioManager.playSoundEffect(Sounds.SUCCESS);
        Preferences.setStartActivity(this);
        finish(); //TODO check how it's done in MainActvity to go to another activity
    }
}
