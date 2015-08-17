package com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity;

import android.os.Bundle;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.BaseCardAdapter;
import com.google.android.glass.widget.CardScrollView;

/**
 * Created by Tilen on 17.8.2015.
 */
public abstract class BaseMultiLayoutActivity extends BaseActivity {
    protected CardScrollView mCardScroller;
    protected BaseCardAdapter mCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCardScroller = new CardScrollView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
        setTextForFooter("", -1);
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

    /**
   @param position - on which card don't set the text (see onSpeechStateChanged, why this is
        implemented)
     */
    protected void setTextForFooter(String resultText, int position)
    {
        mCardAdapter.setTextForFooter(resultText);
        for(int i = 0; i < mCardScroller.getChildCount();i++)
        {
            if(i == position)
                continue;
            mCardAdapter.setTextForFooterView(mCardScroller.getChildAt(i));
        }
    }
}
