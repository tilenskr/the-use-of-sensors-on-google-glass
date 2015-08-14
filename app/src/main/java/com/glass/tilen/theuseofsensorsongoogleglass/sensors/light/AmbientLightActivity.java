package com.glass.tilen.theuseofsensorsongoogleglass.sensors.light;

import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glass.tilen.theuseofsensorsongoogleglass.BaseActivity;
import com.glass.tilen.theuseofsensorsongoogleglass.R;
import com.glass.tilen.theuseofsensorsongoogleglass.animations.FrequentAnimations;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.manager.MainSensorManager;
import com.glass.tilen.theuseofsensorsongoogleglass.speechrecognition.SpeechRecognition;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.BitmapBrightnessTask;
import com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils.SoundPlayer;
import com.google.android.glass.media.Sounds;

public class AmbientLightActivity extends BaseActivity implements SpeechRecognition.SpeechRecognitionCallback,
        MainSensorManager.MainSensorManagerCallback, BitmapBrightnessTask.BitmapBrightnessTasksCallback{
    private ImageView ivPicture;
    private TextView tvFooter;
    private MainSensorManager mainSensorManager;
    private BitmapBrightnessTask mBitmapBrightnessTask;
    private SoundPlayer mSoundPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambient_light_layout);
        mSpeechRecognition = new SpeechRecognition(this, this, SpeechRecognition.KEYWORD_NAVIGATION_ALL);
        ivPicture = (ImageView) findViewById(R.id.ivPicture);
        tvFooter = (TextView) findViewById(R.id.tvFooter);
        mainSensorManager = MainSensorManager.getInstance(this);
        mainSensorManager.setSensor(Sensor.TYPE_LIGHT, this);
        mBitmapBrightnessTask = new BitmapBrightnessTask();
        mSoundPlayer = new SoundPlayer(this);
        mSoundPlayer.setSounds(R.raw.zombie_death);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to go to pause and change state of SpeechRecognizer will happen rarely, so we will not
        // handle setting footer TextView to "". Maybe later. //TODO check if this will slow program and make glass hotter
        tvFooter.setText("");
        mainSensorManager.registerSensor();
        //BitmapBrightnessTask.getBrightnessBitmap(this, this, R.drawable.zombie, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPlayer.stop();
        mainSensorManager.unRegisterSensor();
    }

    @Override
    public void onSpeechStateChanged(String resultText) {
        if(resultText.equals(""))
            resultText = getString(R.string.speak_navigate);
        FrequentAnimations.fadeIn(tvFooter, resultText);
    }

    @Override
    public void onSpeechResult(String text) {

        if(text.equals(getString(R.string.backward)))
        {
            mAudioManager.playSoundEffect(Sounds.DISMISSED);
            finish();
        }
    }

    private int a = 0;
    @Override
    public void onSensorValueChanged(float[] values) {
        a+= 15;
        mBitmapBrightnessTask.getBrightnessBitmap(this, this, R.drawable.zombie, values);
        //tvFooter.setText(String.valueOf(values[0])); // for testing purposes
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
}
