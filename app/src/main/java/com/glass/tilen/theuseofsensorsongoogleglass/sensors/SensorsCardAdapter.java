package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.BaseCardAdapter;

/**
 * Created by Tilen on 9.8.2015.
 */
public class SensorsCardAdapter extends BaseCardAdapter {

    public SensorsCardAdapter(Context mContext) {
        this.mContext = mContext;
        this.textForFooter = "";
    }

    @Override
    public int getCount() {
        return SensorsCard.values().length;
    }

    @Override
    public Object getItem(int i) {
        return SensorsCard.values()[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View sensorsLayout = createBaseView(R.layout.sensors_layout);
        populateView(sensorsLayout, position);
        return sensorsLayout;
    }

    private void populateView(View sensorLayout, int position)
    {
        SensorsCard mSensorsCard = SensorsCard.values()[position];
        TextView tvTitle = (TextView) sensorLayout.findViewById(R.id.tvSensorTitle);
        ImageView ivPicture = (ImageView) sensorLayout.findViewById(R.id.ivRightPicture);
        tvTitle.setText(mSensorsCard.titleId);
        ivPicture.setImageResource(mSensorsCard.imageId);
        setTextForFooterView(sensorLayout);

    }
    @Override
    public int getPosition(Object o) {
        for (int i = 0; i <  SensorsCard.values().length; i++) {
            if (getItem(i).equals(o)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }

    public enum SensorsCard
    {
        GRAPH_SENSORS(R.string.sensors_graphs_title, R.drawable.ic_graph),
        AMBIENT_LIGHT(R.string.sensors_ambient_light_title, R.drawable.ic_ambient_light),
        HEAD_DETECTION(R.string.sensors_head_detection_title, R.drawable.ic_on_head_detection),
        REPEAT_TUTORIAL (R.string.sensors_repeat_title, R.drawable.ic_repeat),
        SENSORS_OVERVIEW (R.string.sensors_overview_title, R.drawable.ic_overview),
        SETTINGS(R.string.sensors_settings_title, R.drawable.ic_settings);

        private int titleId;
        private int imageId;

        SensorsCard(int titleId, int imageId) {
            this.titleId = titleId;
            this.imageId = imageId;
        }
    }
}
