package com.glass.tilen.theuseofsensorsongoogleglass.tutorial;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.checkmark.CheckMarkView;
import com.glass.tilen.theuseofsensorsongoogleglass.customviews.CustomCardScrollView;
import com.glass.tilen.theuseofsensorsongoogleglass.gestures.TutorialGestures;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Preferences;
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
public class TutorialActivity extends Activity implements TutorialGestures.OnGestureCallback, AdapterView.OnItemClickListener {
    private CustomCardScrollView mCardScroller;
    private TutorialCardAdapter mCardAdapter;
    private TutorialGestures mGestureDetector;
    private AudioManager mAudioManager;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }



    /** called first (before we used (recommended) onGenericMotionEvent()) **/
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        return mGestureDetector.onMotionEvent(ev);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Global.LogDebug("TutorialActivty.onItemClick()");
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
        mCardScroller.animate(mCardAdapter.getCount()-1 , CardScrollView.Animation.INSERTION);
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
                    int id = -1;
                    if (gesture == Gesture.SWIPE_LEFT) {
                        id = R.id.cvCheckMark;
                        returnValue = true;
                    } else if (gesture == Gesture.SWIPE_RIGHT) {
                        id = R.id.cvCheckMark2;
                        returnValue = true;
                    }
                    if (returnValue == true) {
                        //setCheckMarkAndProceedForGesture(mTutorialCard, id); //TODO setProperCard
                    }
                    break;
                case SWIPEDOWN:
                    if (gesture == Gesture.SWIPE_DOWN) {
                        returnValue = true;
                        setCheckMarkAndProceedForGesture(mTutorialCard, TutorialCardAdapter.TutorialCard.SWIPING, R.id.cvCheckMark);
                    }
                    break;
            }
        return returnValue;
    }


    private void setCheckMarkAndProceedForGesture(TutorialCardAdapter.TutorialCard currentCard, TutorialCardAdapter.TutorialCard nextCard, int id)
    {
            View tutorialLayout = mCardScroller.getSelectedView();
            CheckMarkView cvMarkView = (CheckMarkView) tutorialLayout.findViewById(id);
            if (!cvMarkView.isChecked()) {
                cvMarkView.toggle();
                currentCard.setCheckMarkPressed();
            }
            Global.LogDebug("TutorialActivity.setCheckMarkAndProceedForGesture():  " + currentCard + ".getCheckMarkPressed(): " + currentCard.getCheckMarkPressed());
            if (currentCard.getCheckMarkPressed() == currentCard.getCheckMarkCount()) {
                currentCard.setHasBeenDone(true);
                proceedWithNextCard(nextCard);

        }

    }
    private void proceedWithNextCard(final TutorialCardAdapter.TutorialCard mTutorialCard)
    {
        Global.LogDebug("TutorialActivity.proceedWithNextCard() " + mTutorialCard);
        mAudioManager.playSoundEffect(Sounds.SUCCESS);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertCardWithAnimation(mTutorialCard);
            }
        }, CheckMarkView.CHECK_MARK_ANIMATION_DURATION);

    }
}
