package com.glass.tilen.theuseofsensorsongoogleglass.sensors.light;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.inheritance.activity.SingleLayoutActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.BitmapBrightnessTask;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.SoundPlayer;
import com.glass.tilen.theuseofsensorsongoogleglass.settings.Global;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;

public class AmbientLightActivity extends SingleLayoutActivity implements
        MainSensorManager.MainSensorManagerCallback, BitmapBrightnessTask.BitmapBrightnessTasksCallback{

    private ImageView ivPicture;
    private MainSensorManager mainSensorManager;
    private BitmapBrightnessTask mBitmapBrightnessTask;
    private SoundPlayer mSoundPlayer;

    private TextView tvAmbientValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambient_light_layout);
        tvAmbientValue = (TextView) findViewById(R.id.tvAmbientValue);
        tvFooter = (TextView) findViewById(R.id.tvFooter);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        mainSensorManager = MainSensorManager.getInstance(this);
        mainSensorManager.setSensorCallback(this);
        mainSensorManager.setSensor(Sensor.TYPE_LIGHT);
        mBitmapBrightnessTask = new BitmapBrightnessTask();
        mSoundPlayer = new SoundPlayer(this);
        mSoundPlayer.setSounds(R.raw.zombie_death);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpeechRecognition.startSpeechRecognition(SpeechRecognition.KEYWORD_NAVIGATION_BACK);
        mainSensorManager.registerSensor(1);
}

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPlayer.stop();
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onSensorValueChanged(float[] values) {
        mBitmapBrightnessTask.getBrightnessBitmap(this, this, R.drawable.zombie, values[0]);
        // if it's on we will show value in the corner where it should be timestamp
        if(Global.isTestingOn())
            tvAmbientValue.setText(String.valueOf(values[0]));
    }

    @Override
    public void onBitmapLoaded(Bitmap bmp) {
        ivPicture.setImageBitmap(bmp);
        ivPicture.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlaySoundEffect() {
       mSoundPlayer.play(R.raw.zombie_death, false);
    }

    @Override
    public void onFinalValue(int value) {
        // if it's on we will show value in the corner where it should be timestamp
        //if(Global.isTestingOn())
            //tvAmbientValue.setText(String.valueOf(value));
    }
}
