package com.glass.tilen.theuseofsensorsongoogleglass.sensors.overview;

import android.content.Context;
import android.hardware.Sensor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.BaseCardAdapter;

import java.util.List;

/**
 * Created by Tilen on 9.8.2015.
 */
public class OverviewCardAdapter extends BaseCardAdapter {
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
        View overviewLayout = createBaseView(R.layout.overview_layout);
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
        setTextForFooterView(overviewLayout);
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
}
