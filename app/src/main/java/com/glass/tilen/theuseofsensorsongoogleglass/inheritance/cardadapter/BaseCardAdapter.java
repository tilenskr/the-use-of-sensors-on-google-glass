package com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.interface_.BaseCardCommunicator;
import com.google.android.glass.widget.CardScrollAdapter;

/**
 * Created by Tilen on 17.8.2015.
 */
public abstract class BaseCardAdapter extends CardScrollAdapter implements BaseCardCommunicator {
    protected String textForFooter;
    protected Context mContext;

    public BaseCardCommunicator getCommunicator()
    {
        throw new RuntimeException("Stub!");
    }

    protected View createBaseView(int layoutId)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View baseView = layoutInflater.inflate(layoutId, null);
        return baseView;
    }

    public void setTextForFooter(String textToDisplay)
    {
        this.textForFooter = textToDisplay;
    }

    public void setTextForFooterView(View layout)
    {
        TextView tvFooter = (TextView) layout.findViewById(R.id.tvFooter);
        tvFooter.setText(textForFooter);
    }

    public void setAnimationForFooterTextView(View sensorsLayout, String text)
    {
        View tvFooter = sensorsLayout.findViewById(R.id.tvFooter);
        // we use the same duration as animation for CheckMarkView
        FrequentAnimations.fadeIn(tvFooter, text);
    }
}
