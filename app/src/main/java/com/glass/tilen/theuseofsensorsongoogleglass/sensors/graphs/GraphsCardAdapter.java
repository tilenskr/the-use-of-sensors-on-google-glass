package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.content.Context;
import android.hardware.Sensor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.cardadapter.BaseCardAdapter;

/**
 * Created by Tilen on 19.8.2015.
 */
public class GraphsCardAdapter extends BaseCardAdapter implements GraphsCardAdapterCommunicator {

    public GraphsCardAdapter(Context mContext) {
        this.mContext = mContext;
        this.textForFooter = "";
    }

    @Override
    public GraphsCardAdapterCommunicator getCommunicator()
    {
        return this;
    }

    @Override
    public int getCount() {
        return GraphsCard.values().length;
    }

    @Override
    public Object getItem(int i) {
        return GraphsCard.values()[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View overviewLayout = createBaseView(R.layout.graphs_layout);
        populateView(overviewLayout, position);
        return overviewLayout;
    }

    private void populateView(View graphsLayout, int position)
    {
        GraphsCard mGraphsCard = GraphsCard.values()[position];
        TextView tvSensor = (TextView) graphsLayout.findViewById(R.id.tvSensor);
        LineChart mLineChart = (LineChart) graphsLayout.findViewById(R.id.lineChart);
        tvSensor.setText(mGraphsCard.sensor);
        Chart mChart = new Chart(mLineChart, mContext);
        graphsLayout.setTag(mChart);
        setTextForFooterView(graphsLayout);
    }

    @Override
    public int getPosition(Object o) {
        for (int i = 0; i < GraphsCard.values().length; i++) {
            if (getItem(i).equals(o)) {
                return i;
            }
        }
        return AdapterView.INVALID_POSITION;
    }

    @Override
    public void addNewPoints(View graphsLayout, float[] values)
    {
        Chart mChart = (Chart) graphsLayout.getTag();
        mChart.addSensorValues(values);
    }

    @Override
    public void changeAxis(View graphsLayout) {
        Chart mChart = (Chart) graphsLayout.getTag();
        mChart.changeAxis();
    }

    @Override
    public void setChartLimits(View graphsLayout, float minValue, float maxValue) {
        Chart mChart = (Chart) graphsLayout.getTag();
        mChart.setChartLimits(minValue, maxValue);
    }

    @Override
    public void setAdaptiveChartLimits(View graphsLayout) {
        Chart mChart = (Chart) graphsLayout.getTag();
        mChart.setAdaptiveChartLimits();
    }

    public enum GraphsCard
    {
        ACCELEROMETER(R.string.graphs_accelerometer, Sensor.TYPE_ACCELEROMETER),
        GRAVITY(R.string.graphs_gravity, Sensor.TYPE_GRAVITY),
        GYROSCOPE(R.string.graphs_gyroscope, Sensor.TYPE_GYROSCOPE),
        LINEAR_ACCELERATION(R.string.graphs_linear, Sensor.TYPE_LINEAR_ACCELERATION),
        MAGNETIC_FIELD(R.string.graphs_magnetic, Sensor.TYPE_MAGNETIC_FIELD);
        //ROTATION_VECTOR(R.string.graphs_rotation, Sensor.TYPE_ROTATION_VECTOR);

        private int sensor;
        private int sensorType;

        public int getSensorType() {
            return sensorType;
        }

        GraphsCard(int sensor, int sensorType) {
            this.sensor = sensor;
            this.sensorType = sensorType;
        }
    }
}
