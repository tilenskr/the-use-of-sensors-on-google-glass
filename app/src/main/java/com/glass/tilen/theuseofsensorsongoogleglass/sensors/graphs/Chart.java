package com.glass.tilen.theuseofsensorsongoogleglass.sensors.graphs;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

/**
 * Created by Tilen on 19.8.2015.
 */
public class Chart {
    private LineChart mChart;


    public Chart(LineChart mChart)
    {
        this.mChart = mChart;
        setChart();
    }

    private void setChart()
    {
        /** general chart settings **/
        // no description text
        mChart.setDescription("");
        mChart.setDrawGridBackground(false);
        mChart.setHighlightEnabled(true);

        /** LineData **/
        LineData data = new LineData();
       data.setValueTextColor(Color.WHITE);
        // add empty data
        mChart.setData(data);

        /* Legend (only possible after setting data)*/
        Legend l = mChart.getLegend();
        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        /** Axis **/
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        /** LineDataSet **/


    }

    private LineDataSet createDataSet(String name, int linecolor, int circleColor)
    {
        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setDrawValues(false);

        return set;
    }

    public void addSensorValues(float[] values)
    {
        LineData lineData = mChart.getData();
        LineDataSet lineDataSet;
        for(int i = 0; i < 3; i++)
        {
            lineDataSet = lineData.getDataSetByIndex(i);
            // TODO add single value to set line.
        }
        mChart.notifyDataSetChanged();
        // limit the number of visible entries
        mChart.setVisibleXRangeMaximum(120);
        // move to the latest entry
        mChart.moveViewToX(lineData.getXValCount() - 121);

    }

}
