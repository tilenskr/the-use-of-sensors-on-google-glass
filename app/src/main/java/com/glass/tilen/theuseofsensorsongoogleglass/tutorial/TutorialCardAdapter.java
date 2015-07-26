package com.glass.tilen.theuseofsensorsongoogleglass.tutorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.checkmark.CheckMarkView;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tilen on 14.7.2015.
 */
public class TutorialCardAdapter extends CardScrollAdapter {

    private final List<TutorialCard> mTutorialCards;
    /** CardBuilder holds also a reference to the view, because convertView in getView is always null **/
    private final List<View> mView;
    private final Context mContext;
    private String textForFooter;

    public TutorialCardAdapter(Context mContext) {
        mTutorialCards = new ArrayList<TutorialCard>();
        mView = new ArrayList<View>();
        this.mContext = mContext;
        this.textForFooter = "";
    }

    @Override
    public int getCount() {
        return mTutorialCards.size();
    }

    @Override
    public Object getItem(int i) {
        return mTutorialCards.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate our view
        Global.InfoDebug("TutorialCardAdapter.getView(): position: " + position + " view: " + convertView);

        if(mTutorialCards.get(position).getHasBeenDone())
            return mView.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View tutorialLayout = layoutInflater.inflate(R.layout.tutorial_layout, null);
        TutorialCard mTutorialCard = mTutorialCards.get(position);
        TextView tvTitle = (TextView) tutorialLayout.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) tutorialLayout.findViewById(R.id.tvDescription);
        CheckMarkView cvCheckMark = (CheckMarkView) tutorialLayout.findViewById(R.id.cvCheckMark);
        CheckMarkView cvCheckMark2 = (CheckMarkView) tutorialLayout.findViewById(R.id.cvCheckMark2);
        tvTitle.setText(mTutorialCard.titleId);
        tvDescription.setText(mTutorialCard.descriptionId);
        int checkMarkCount = mTutorialCard.checkMarkCount;
        //default is one checkMark
        if(checkMarkCount == 0)
            cvCheckMark.setVisibility(View.GONE);
        else if(checkMarkCount == 2)
            cvCheckMark2.setVisibility(View.VISIBLE);
        if(textForFooter != "") {
            TextView tvFooter = (TextView) tutorialLayout.findViewById(R.id.tvFooter);
            tvFooter.setText(textForFooter);
        }
        mView.add(tutorialLayout);
        return tutorialLayout;
    }

    @Override
    public int getPosition(Object o) {

        for (int i = 0; i < mTutorialCards.size(); i++) {
            if (getItem(i).equals(o)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }


    public void insertCardWithoutAnimation(TutorialCard mTutorialCard) {
        mTutorialCards.add(mTutorialCard);
    }

    public TutorialCard getTutorialCardAt(int position)
    {
        return mTutorialCards.get(position);
    }


    public void setTextForFooter(String textToDisplay)
    {
        this.textForFooter = textToDisplay;
        for(View tutorialLayout : mView)
        {
            TextView tvFooter = (TextView) tutorialLayout.findViewById(R.id.tvFooter);
            tvFooter.setText(textToDisplay);
        }
    }

    public enum TutorialCard
    {
        TAP_TOUCHPAD(R.string.tutorial_welcome_title, R.string.tutorial_welcome_description, 1),
        SWIPEDOWN(R.string.tutorial_swipe_down_title, R.string.tutorial_swipe_down_description, 1),
        SWIPING (R.string.tutorial_swiping_title, R.string.tutorial_swiping_description, 2);

        private int titleId;
        private int descriptionId;
        private int checkMarkCount;
        private int checkMarkPressed;
        private boolean hasBeenDone;

        public int getCheckMarkPressed() {
            return checkMarkPressed;
        }

        public void setCheckMarkPressed() {
            this.checkMarkPressed++;
        }
        public int getCheckMarkCount() {
            return checkMarkCount;
        }

        public void setHasBeenDone(boolean hasBeenDone) {
            this.hasBeenDone = hasBeenDone;
        }

        public boolean getHasBeenDone()
        {
            return hasBeenDone;
        }

        public void clearCheckMarkPressed()
        {
            checkMarkPressed = 0;
        }

        TutorialCard(int titleId, int descriptionId, int checkMarkCount) {
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.checkMarkCount = checkMarkCount;
            this.checkMarkPressed = 0;
            this.hasBeenDone = false;
        }
    }

}
