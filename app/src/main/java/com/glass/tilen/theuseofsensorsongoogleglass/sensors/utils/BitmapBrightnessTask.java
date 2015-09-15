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
        void onFinalValue(int value);
    }


    public void getBrightnessBitmap(final Context context,final BitmapBrightnessTasksCallback mCallback , final int pictureId, final float value)
    {
        new AsyncTask<Void, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), pictureId);
                int newValue = Utils.getBrightnessValue(value);
                publishProgress(newValue);
                return Utils.setBrightness(bitmap, newValue);
                }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                // pass forward final value, that is used for ColorMatrix
                mCallback.onFinalValue(values[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(value < 4)
                    mCallback.onPlaySoundEffect();
                if(bitmap != null)
                    mCallback.onBitmapLoaded(bitmap);
            }
        }.execute();
    }
}