package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.final_project.entity.Values;

import static com.example.final_project.MainActivity.musicBackgroundService;

public class SettingActivity extends AppCompatActivity {
    Spinner spinner;
    String[] sizeBoard;
    int buttonEffect = R.raw.choose_sound;
    int musicMode, effectMode;
    EditText textSizeHP;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        settingSpinner();
        settingSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicBackgroundService != null&&musicBackgroundService.isPlaying==true) {
           musicBackgroundService.resumeMusic();
        }
    }

    private void settingSpinner() {
        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        textSizeHP = findViewById(R.id.editTextNumberDecimal);
        sizeBoard = getResources().getStringArray(R.array.board_size_array);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Values.board_size = Integer.parseInt(sizeBoard[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void settingSwitch() {
        pref = getSharedPreferences("GAME_SETTING", MODE_PRIVATE); // ten cua file luu tru
        editor = pref.edit();
        musicMode = pref.getInt("MUSIC", -1);
        effectMode = pref.getInt("EFFECT", -1);
        Switch swM = (Switch) findViewById(R.id.isMusic);
        if(musicMode==1)swM.setChecked(true);
        else swM.setChecked(false);
        swM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    editor.remove("MUSIC");
                    editor.commit();
                    editor.putInt("MUSIC", 1);
                    editor.commit();
                    musicBackgroundService.isPlaying = true;
                    musicBackgroundService.resumeMusic();
                } else {
                    // The toggle is disabled
                    editor.remove("MUSIC");
                    editor.commit();
                    editor.putInt("MUSIC", 0);
                    editor.commit();
                    musicBackgroundService.isPlaying = false;
                    musicBackgroundService.pauseMusic();
                }
            }
        });

        Switch swE = (Switch) findViewById(R.id.isEffect);
        swE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    editor.remove("EFFECT");
                    editor.commit();
                    editor.putInt("EFFECT", 1);
                    editor.commit();
                } else {
                    // The toggle is disabled
                    editor.remove("EFFECT");
                    editor.commit();
                    editor.putInt("EFFECT", 0);
                    editor.commit();
                }
            }
        });
    }

    void save() {
        Values.HP = Integer.parseInt(textSizeHP.getText().toString());
    }

    public void saveOnClick(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void backOnClick(View view) {
        MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
        mPlayer.start();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}