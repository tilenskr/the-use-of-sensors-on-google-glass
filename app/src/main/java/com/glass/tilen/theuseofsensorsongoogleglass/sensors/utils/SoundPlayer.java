package com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Tilen on 14.8.2015.
 */
public class SoundPlayer {
    private Context mContext;
    private int soundId;
    private MediaPlayer mMediaPlayer;

    public SoundPlayer(Context mContext, int soundId)
    {
        this.mContext = mContext;
        this.soundId = soundId;
        mMediaPlayer = MediaPlayer.create(mContext, soundId);
    }
    public void play (boolean loop)
    {
        if(mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(mContext, soundId);
        mMediaPlayer.setLooping(loop);
        mMediaPlayer.start();
    }

    public void stop()
    {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
