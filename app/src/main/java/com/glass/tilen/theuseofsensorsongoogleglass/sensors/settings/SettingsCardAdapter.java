package com.glass.tilen.theuseofsensorsongoogleglass.sensors.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.BaseCardAdapter;

import java.util.HashMap;

/**
 * Created by Tilen on 12.8.2015.
 */
public class SettingsCardAdapter extends BaseCardAdapter implements SettingsCardAdapterCommunicator {
    private HashMap<SettingsCard, Boolean> actions;

    public SettingsCardAdapter(Context mContext, HashMap<SettingsCard, Boolean> actions) {
        this.mContext = mContext;
        this.textForFooter = "";
        this.actions = actions;
    }

    @Override
    public SettingsCardAdapterCommunicator getCommunicator()
    {
        return this;
    }

    @Override
    public int getCount() {
        return SettingsCard.values().length;
    }

    @Override
    public SettingsCard getItem(int i) {
        return SettingsCard.values()[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View settingsLayout = createBaseView(R.layout.settings_layout);
        populateView(settingsLayout, position);
        return settingsLayout;
    }

    private void populateView(View settingsLayout, int position)
    {
        SettingsCard mSettingsCard = SettingsCard.values()[position];
        TextView tvTitle = (TextView) settingsLayout.findViewById(R.id.tvSettingsTitle);
        TextView tvAction = (TextView) settingsLayout.findViewById(R.id.tvOverviewAction);
        ImageView ivPicture = (ImageView) settingsLayout.findViewById(R.id.ivSettingsIcon);

        tvTitle.setText(mSettingsCard.titleId);
        tvAction.setText(getActionTextId(mSettingsCard));
        tvAction.setTypeface(null, Typeface.BOLD); // it works :)
        ivPicture.setImageResource(mSettingsCard.imageId);
        setTextForFooterView(settingsLayout);
    }

    @Override
    public int getPosition(Object o) {
        for (int i = 0; i <  SettingsCard.values().length; i++) {
            if (getItem(i).equals(o)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }

    @Override
    public void setFadeOutFadeInAnimationForFooterTextView(View sensorsLayout, String text)
    {
        View tvFooter = sensorsLayout.findViewById(R.id.tvFooter);
        // we use the same duration as animation for CheckMarkView
        FrequentAnimations.fadeOutFadeIn(tvFooter, text);
    }

    private int getActionTextId(SettingsCard mSettingsCard)
    {
        boolean actionValue = actions.get(mSettingsCard);
        int text;
        if(actionValue == true)
            text = mSettingsCard.actionOnId;
        else
           text = mSettingsCard.actionOffId;
        return text;
    }

    @Override
    public void changeActionText(SettingsCard mSettingsCard, View view)
    {
        boolean actionValue = !actions.get(mSettingsCard);
        actions.put(mSettingsCard, actionValue);
        View tvAction = view.findViewById(R.id.tvOverviewAction);
        String text = mContext.getString(getActionTextId(mSettingsCard));
        FrequentAnimations.fadeOutFadeIn(tvAction, text);
    }

    public enum SettingsCard
    {
        SPEECH_RECOGNITION(R.string.settings_speech_recognition_title, R.string.message_on, R.string.message_off, R.drawable.ic_voice_recognition);

        private int titleId;
        private int actionOnId;
        private int actionOffId;
        private int imageId;

        SettingsCard(int titleId, int actionOnId, int actionOffId, int imageId) {
            this.titleId = titleId;
            this.actionOnId = actionOnId;
            this.actionOffId = actionOffId;
            this.imageId = imageId;
        }
    }
}