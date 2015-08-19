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

    public static Bitmap setBrightness(Bitmap src, float[] value, float value1[]) {
        if(value == null) return null;
        value = lowPass(value, value1);
        if(value[0] > 1000)
            value[0] = Float.valueOf(1000);
        final int newValue =  getCalculatedValue(value[0]);
        Bitmap bmpOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1, 0, 0, 0, newValue, 0, 1,
                0, 0, newValue,//
                0, 0, 1, 0, newValue, 0, 0, 0, 1, 0 });
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Canvas canvas = new Canvas(bmpOut);
        // CanvasBitmap??dstBitmapsrcBitmap
        canvas.drawBitmap(src, 0, 0, paint);
        return bmpOut;
    }
    private static int getCalculatedValue(float initialValue)
    {
        //quadratic equation; points: (1,0), (15, -138), (1000, -255)
        int newValue = (int) ( 0.009748109240495* Math.pow(initialValue,2) - 10.013112604991 * initialValue
                +10.00336449575);
        //Global.TestDebug("getcalculated value: " + newValue);
        return newValue;
    }
    // for smoothing values, how the new value effected previous value
    // not really sure if we needed
    private static float[] lowPass( float input[], float output[] ) {
        if ( input == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.15f * (input[i] - output[i]);
        }
        return output;
    }

    public static float[] normalizeArray(float[] values)
    {
        double sum = 0;
        for(float value : values)
            sum += value * value;
        double sqrtSum = Math.sqrt(sum);
        float[] newValues = new float[values.length];
        for(int i = 0; i < values.length; i++)
            newValues[i] = (float) (values[i] / sqrtSum);
        return newValues;
    }
}
