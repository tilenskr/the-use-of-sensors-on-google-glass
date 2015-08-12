package com.glass.tilen.theuseofsensorsongoogleglass.sensors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.google.android.glass.widget.CardScrollAdapter;

/**
 * Created by Tilen on 9.8.2015.
 */
public class SensorsCardAdapter extends CardScrollAdapter {
    private Context mContext;
    private String textForFooter;

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
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View sensorsLayout = layoutInflater.inflate(R.layout.sensors_layout, null);
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
        if(textForFooter != "") {
            TextView tvFooter = (TextView) sensorLayout.findViewById(R.id.tvFooter);
            tvFooter.setText(textForFooter);
        }
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

    public void  setAnimationForFooterTextView(View sensorsLayout)
    {
        View tvFooter = sensorsLayout.findViewById(R.id.tvFooter);
        // we use the same duration as animation for CheckMarkView
        FrequentAnimations.fadeIn(tvFooter, "");
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
