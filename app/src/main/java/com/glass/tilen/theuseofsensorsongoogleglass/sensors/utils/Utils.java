package com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by Tilen on 13.8.2015.
 */
public class Utils {

    public static int getBrightnessValue(float value)
    {
        if (value > 1000)
            value = Float.valueOf(1000);
        int newValue = getCalculatedValue(value);
        //Global.TestDebug("Utils.setBrightness(): value: " + newValue);
        return newValue;
    }

    public static Bitmap setBrightness(Bitmap src, int newValue) {
        Bitmap bmpOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, newValue, 0, 1,
                0, 0, newValue,//
                0, 0, 1, 0, newValue, 0, 0, 0, 1, 0});
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmpOut);
        canvas.drawBitmap(src, 0, 0, paint);
        return bmpOut;
    }

    private static int getCalculatedValue(float initialValue) {
        //quadratic equation; points: (1,0), (15, -138), (1000, -255)
        int newValue = (int) (0.009748109240495 * Math.pow(initialValue, 2) - 10.013112604991 * initialValue
                + 10.00336449575);
        //Global.TestDebug("Utils: getCalculatedValue(): Value: " + newValue);
        return newValue;
    }

    /**
     * For smoothing values - previous value effects new value
     */
    public static float[] lowPass(float input[], float output[]) {
        if (input == null) return output;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + 0.15f * (input[i] - output[i]);
        }
        return output;
    }

    public static float[] normalizeArray(float[] values) {
        double sum = 0;
        for (float value : values)
            sum += value * value;
        double sqrtSum = Math.sqrt(sum);
        float[] newValues = new float[values.length];
        for (int i = 0; i < values.length; i++)
            newValues[i] = (float) (values[i] / sqrtSum);
        return newValues;
    }

    public static float[] divideWithMaxValue(float[] values, float maximumRange, float multiplication)
    {
        for(int i= 0; i < values.length; i++ ) {
            values[i] = values[i] / maximumRange * multiplication;
            if(values[i] > 1)
                values[i] = 1;
            else if (values[i] < -1)
                values[i] = -1;
        }
        return values;
    }

    public static float getMaxValue(float[] array)
    {
        float maxValue = array[0];
        for(int i=1;i < array.length;i++){
            if(array[i] > maxValue){
                maxValue = array[i];
            }
        }
        return maxValue;
    }
    public static float getMinValue(float[] array)
    {
        float minValue = array[0];
        for(int i=1;i<array.length;i++){
            if(array[i] < minValue){
                minValue = array[i];
            }
        }
        return minValue;
    }
}
