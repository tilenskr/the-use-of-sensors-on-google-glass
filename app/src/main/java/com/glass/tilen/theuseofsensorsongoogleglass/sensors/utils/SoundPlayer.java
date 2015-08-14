package com.glass.tilen.theuseofsensorsongoogleglass.sensors.utils;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;

/**
 * Created by Tilen on 14.8.2015.
 */
public class SoundPlayer {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int currentSoundId;
    private HashMap<Integer, Integer> soundsTimes;

    public SoundPlayer(Context mContext)
    {
        this.mContext = mContext;
    }

    /** if we need more than one sound **/
    public void setSounds(int... soundsIds)
    {
        soundsTimes = new HashMap<Integer, Integer>();
        for(int soundId : soundsIds)
            soundsTimes.put(soundId, 0);
    }

    public void play (int soundId, boolean loop)
    {
        if(mMediaPlayer != null)
            if(this.currentSoundId != soundId) {
                soundsTimes.put(this.currentSoundId, mMediaPlayer.getCurrentPosition());
                mMediaPlayer.reset();
                mMediaPlayer = null;
            }
            else if(!mMediaPlayer.isPlaying())
            {
                soundsTimes.put(this.currentSoundId, 0);
                mMediaPlayer.reset();
                mMediaPlayer = null;
            }
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(mContext, soundId);
            mMediaPlayer.setVolume(1, 1);
        }
        if(!mMediaPlayer.isPlaying())
            mMediaPlayer.seekTo(soundsTimes.get(soundId));
        mMediaPlayer.setLooping(loop);
        mMediaPlayer.start();
        this.currentSoundId = soundId;
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
