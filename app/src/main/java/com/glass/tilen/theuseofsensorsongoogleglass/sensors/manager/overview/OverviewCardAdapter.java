package com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.overview;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.checkmark.CheckMarkView;
import com.google.android.glass.widget.CardScrollAdapter;

import java.util.List;

/**
 * Created by Tilen on 9.8.2015.
 */
public class OverviewCardAdapter extends CardScrollAdapter {
    private Context mContext;
    private String textForFooter;
    private List<Sensor> mSensors;

    public OverviewCardAdapter(Context mContext, List<Sensor> mSensors) {
        this.mContext = mContext;
        this.textForFooter = "";
        this.mSensors = mSensors;
    }

    @Override
    public int getCount() {
        return mSensors.size();
    }

    @Override
    public Object getItem(int i) {
        return mSensors.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View overviewLayout = layoutInflater.inflate(R.layout.overview_layout, null);
        populateView(overviewLayout, position);
        return overviewLayout;
    }

    private void populateView(View overviewLayout, int position)
    {
        Sensor mSensor = mSensors.get(position);
        TextView tvTitle = (TextView) overviewLayout.findViewById(R.id.tvOverviewTitle);
        TextView tvVendor = (TextView) overviewLayout.findViewById(R.id.tvOverviewVendor);
        TextView tvVersion = (TextView) overviewLayout.findViewById(R.id.tvOverviewVersion);
        tvTitle.setText(mSensor.getName());
        tvVendor.setText(String.format(mContext.getString(R.string.overview_vendor), mSensor.getVendor()));
        tvVersion.setText(String.format(mContext.getString(R.string.overview_version), mSensor.getVersion()));
        if(textForFooter != "") {
            TextView tvFooter = (TextView) overviewLayout.findViewById(R.id.tvFooter);
            tvFooter.setText(textForFooter);
        }
    }

    @Override
    public int getPosition(Object o) {
        for (int i = 0; i <  mSensors.size(); i++) {
            if (getItem(i).equals(o)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }

    public void  setAnimationForFooterTextView(View sensorsLayout)
    {
        View tvFooter = sensorsLayout.findViewById(R.id.tvFooter);
        // we use the same duration as animation for CheckMarkView
        FrequentAnimations.fadeIn(tvFooter, CheckMarkView.CHECK_MARK_ANIMATION_DURATION);
    }
    public void setTextForFooter(String textToDisplay)
    {
        this.textForFooter = textToDisplay;
    }

    public void setTextForView(View sensorsLayout)
    {
        TextView tvFooter = (TextView) sensorsLayout.findViewById(R.id.tvFooter);
        tvFooter.setText(textForFooter);
    }
}
