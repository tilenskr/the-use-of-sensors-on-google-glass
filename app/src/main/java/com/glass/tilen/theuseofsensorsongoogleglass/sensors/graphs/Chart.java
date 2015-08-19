package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.glass.tilen.theuseofsensorsongoogleglass.R;

/**
 * Created by Tilen on 19.8.2015.
 */
public class Chart {
    private Context mContext;
    private LineChart mChart;
    /** 0 - only X-axis, 1 - only Y-axis, 2 - only Z-Axis, 3 - all axes **/
    private int axis;

    public Chart(LineChart mChart, Context mContext)
    {
        this.mChart = mChart;
        this.mContext = mContext;
        axis = 3;
        setChart();
    }

    private void setChart()
    {
        /** general chart settings **/
        // no description text
        mChart.setDescription("");
        mChart.setDrawGridBackground(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setTouchEnabled(false);
        /** LineData **/
        LineData data = new LineData();
       data.setValueTextColor(Color.WHITE);
        // add empty data
        mChart.setData(data);

        /* Legend (only possible after setting data)*/
        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        /** Axis **/
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(1);
        leftAxis.setAxisMinValue(-1);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        /** LineDataSet **/
        Resources resources = mContext.getResources();
        LineDataSet dataSetX = createDataSet(resources.getString(R.string.x_axis),
                resources.getColor(R.color.x_axis));
        LineDataSet dataSetY = createDataSet(resources.getString(R.string.y_axis),
                resources.getColor(R.color.y_axis));
        LineDataSet dataSetZ = createDataSet(resources.getString(R.string.z_axis),
                resources.getColor(R.color.z_axis));
        data.addDataSet(dataSetX);
        data.addDataSet(dataSetY);
        data.addDataSet(dataSetZ);
    }

    private LineDataSet createDataSet(String name, int lineColor)
    {
        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(lineColor);
        set.setCircleColor(lineColor);
        set.setLineWidth(2f);
        set.setCircleSize(3);
        set.setFillAlpha(65);
        set.setFillColor(lineColor);
        set.setDrawValues(false);
        return set;
    }

    public void addSensorValues(float[] values)
    {
        LineData lineData = mChart.getData();
        lineData.addXValue(String.valueOf(lineData.getXValCount()));
        for(int i = 0; i < 3; i++)
        {
            if(axis != i && axis != 3) {
                LineDataSet mLineDataSet = lineData.getDataSetByIndex(i);
                mLineDataSet.clear();
                continue;
            }
            lineData.addEntry(new Entry(values[i], (lineData.getXValCount() - 1)), i);
        }

        mChart.notifyDataSetChanged();
        // limit the number of visible entries
        mChart.setVisibleXRangeMaximum(20);

        // move to the latest entry
        mChart.moveViewToX(lineData.getXValCount() - 21);
    }

    public void changeAxis()
    {
        axis++;
        axis %= 4;
    }

}
