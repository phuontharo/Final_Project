package com.example.final_project.controller;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.example.final_project.R;

public class BackgroundGameMusic extends Service implements MediaPlayer.OnErrorListener  {
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    int musicID = R.raw.game_background;
    int leftVol = 50, rightVol = 50;
    private int length = 0;

    public BackgroundGameMusic() {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(this, musicID);
        mPlayer.setOnErrorListener(this);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(leftVol, rightVol);
        }
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mPlayer != null) {
            mPlayer.start();
        }
        return START_NOT_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                length = mPlayer.getCurrentPosition();
            }
        }
    }

    public void resumeMusic() {
        if (mPlayer != null) {
            if (!mPlayer.isPlaying()) {
                mPlayer.seekTo(length);
                mPlayer.start();
            }
        }
    }

    public void setMusic(int uri) {
        musicID = uri;
    }

    public void setMusicVolume(int leftVol, int rightVol) {
        this.leftVol = leftVol;
        this.rightVol = rightVol;
        mPlayer.setVolume(leftVol, rightVol);
    }

    public boolean isMusicMute() {
        if (leftVol == 0 && rightVol == 0)
            return true;
        else return false;
    }

    public void startMusic() {
        mPlayer = MediaPlayer.create(this, musicID);
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(50, 50);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "Music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public class ServiceBinder extends Binder {
        BackgroundGameMusic getService() {
            return BackgroundGameMusic.this;
        }
    }
}
