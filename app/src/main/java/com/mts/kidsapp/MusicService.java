package com.mts.kidsapp;

import static com.mts.kidsapp.PrefUtils.length;
import static com.mts.kidsapp.PrefUtils.player;
import static com.mts.kidsapp.PrefUtils.soundsEnable;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service{

    private SharedPreferences prefsMusic;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefsMusic = getApplicationContext().getSharedPreferences(
                PrefUtils.musicPrefs, MODE_PRIVATE);
        if(prefsMusic.getBoolean("sound", true))
        {
            soundsEnable=true;
            player = MediaPlayer.create(getApplicationContext(),
                    R.raw.app_music);
            player.setLooping(true);
            player.start();

        }
        else {
            soundsEnable=false;
            if(player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    length = player.getCurrentPosition();
                }
            }
        }
        return START_NOT_STICKY;
    }

    public static void pauseMusic()
    {
        if(soundsEnable)
        {
            if(player != null) {
                if (player.isPlaying()) {
                    player.pause();
                    length = player.getCurrentPosition();
                }
            }
        }
    }

    public static void resumeMusic()
    {
        if(soundsEnable)
        {
            if(player != null) {
                if (!player.isPlaying()) {
                    player.seekTo(length);
                    player.start();
                }
            }
        }
    }

    public static void stopMusic()
    {
        if(player != null) {
            if (!player.isPlaying()) {
                player.stop();
                player.release();
                player = null;
            }
        }
    }

    public static void stopMusic2()
    {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!prefsMusic.getBoolean("sound", false))
        {
            if(player != null) {
                stopMusic();
            }
        }
    }

}