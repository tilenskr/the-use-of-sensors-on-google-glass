package com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by Tilen on 13.8.2015.
 */
public class BitmapBrightnessTask   {
    public interface BitmapBrightnessTasksCallback {
        void onBitmapLoaded(Bitmap bmp);
        void onPlaySoundEffect();
    }
    float[] previousValue = null;
    public void getBrightnessBitmap(final Context context,final BitmapBrightnessTasksCallback mCallback , final int pictureId, final float[] value)
    {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), pictureId);
                return Utils.setBrightness(bitmap, previousValue ,value);
                }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                previousValue = value;
                if(value[0] < 4)
                    mCallback.onPlaySoundEffect();
                if(bitmap != null)
                    mCallback.onBitmapLoaded(bitmap);
            }


        }.execute();
    }


}
