package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.final_project.controller.BackgroundMusic;
import com.example.final_project.controller.HomeWatcher;
import com.example.final_project.entity.Values;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private int buttonEffect = R.raw.menu_sound;
    public static BackgroundMusic musicBackgroundService;
    HomeWatcher mHomeWatcher;
    int musicMode, effectMode;

    //setting music
    private boolean mIsBound = false; // link with activity
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            musicBackgroundService = ((BackgroundMusic.ServiceBinder) binder).getService();

            if (musicMode == 0) {
                musicBackgroundService.isPlaying = false;
                musicBackgroundService.pauseMusic();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            musicBackgroundService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getScreen();
        settingMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicBackgroundService != null && musicBackgroundService.isPlaying == true) {
            musicBackgroundService.resumeMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusic.class);
        stopService(music);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }
        if (!isScreenOn) {
            if (musicBackgroundService != null) {
                musicBackgroundService.pauseMusic();
            }
        }
    }

    void doBindService() { // lien ket service
        bindService(new Intent(this, BackgroundMusic.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() { // destroy
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    void getScreen() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Values.board_width = size.x - 100;
        Values.board_height = size.x - 100;
    }

    private void settingMusic() {
        pref = getSharedPreferences("GAME_SETTING", MODE_PRIVATE); // ten cua file luu tru
        editor = pref.edit();
        musicMode = pref.getInt("MUSIC", -1);
        effectMode = pref.getInt("EFFECT", -1);
        //music
        doBindService();
        Intent music = new Intent();
        music.setClass(this, BackgroundMusic.class);
        startService(music);
        // home button - end music
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (musicBackgroundService != null) {
                    musicBackgroundService.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (musicBackgroundService != null) {
                    musicBackgroundService.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
    }

    public void onClickStart(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }

        Intent intent = new Intent(this, Information.class);
        intent.putExtra("mode", "double");
        startActivity(intent);
    }

    public void onClickLan(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }

//        Intent intent = new Intent(this, WaitGuest.class);
//        startActivity(intent);
    }

    public void onClickSetting(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }

        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void onClickQuit(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }

        finish();
        System.exit(0);
    }

    public void muteOnClick(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }
        if (musicBackgroundService == null) return;
        if (musicBackgroundService.isMusicMute()) {
            editor.remove("MUSIC");
            editor.commit();
            editor.putInt("MUSIC", 1);
            editor.commit();
            musicBackgroundService.isPlaying = true;
            musicBackgroundService.resumeMusic();
        } else {
            editor.remove("MUSIC");
            editor.commit();
            editor.putInt("MUSIC", 0);
            editor.commit();
            musicBackgroundService.isPlaying = false;
            musicBackgroundService.pauseMusic();
        }
    }
}