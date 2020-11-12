package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    Spinner spinnerSize, spinnerTime, spinnerHP;
    // String[] sizeBoard;
    int buttonEffect = R.raw.choose_sound;
    int musicMode, effectMode, size_board, hp;
    int time;
    int m, s; // minute, seconds
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        pref = getSharedPreferences("GAME_SETTING", MODE_PRIVATE); // ten cua file luu tru
        editor = pref.edit();
        effectMode = pref.getInt("EFFECT", -1);
        settingSpinner();
        settingSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicBackgroundService != null && musicBackgroundService.isPlaying == true) {
            musicBackgroundService.resumeMusic();
        }
    }

    private void settingSpinner() {
        spinnerSize = findViewById(R.id.spinnerSize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerSize.setAdapter(adapter);
        size_board = pref.getInt("SIZE", -1);
        if (size_board == -1) spinnerSize.setSelection(2);
        else spinnerSize.setSelection(size_board);
        //sizeBoard = getResources().getStringArray(R.array.board_size_array);
        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Values.board_size = Integer.parseInt(sizeBoard[position]);
                int spinner_pos = spinnerSize.getSelectedItemPosition(); // POSITION
                editor.remove("SIZE");
                editor.commit();
                editor.putInt("SIZE", spinner_pos);
                editor.commit();
                String[] size_values = getResources().getStringArray(R.array.board_size_array);
                Values.board_size = Integer.valueOf(size_values[spinner_pos]); // VALUE
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTime = findViewById(R.id.spinnerTime);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.main_time, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTime.setAdapter(adapter);
        time = pref.getInt("TIME", -1);
        if (time == -1) spinnerTime.setSelection(2);
        else spinnerTime.setSelection(time);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int spinner_pos = spinnerTime.getSelectedItemPosition(); // POSITION
                editor.remove("TIME");
                editor.commit();
                editor.putInt("TIME", spinner_pos);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerHP = findViewById(R.id.spinnerHP);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.hp, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerHP.setAdapter(adapter);
        hp = pref.getInt("HP", -1);
        if (hp == -1) spinnerHP.setSelection(5);
        else spinnerHP.setSelection(hp);
        spinnerHP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int spinner_pos = spinnerHP.getSelectedItemPosition(); // POSITION
                editor.remove("HP");
                editor.commit();
                editor.putInt("HP", spinner_pos);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void settingSwitch() {
        musicMode = pref.getInt("MUSIC", -1);
        Switch swM = (Switch) findViewById(R.id.isMusic);
        if (musicMode != 0) swM.setChecked(true);
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

        musicMode = pref.getInt("EFFECT", -1);
        Switch swE = (Switch) findViewById(R.id.isEffect);
        if (effectMode != 0) swE.setChecked(true);
        else swE.setChecked(false);
        swE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    editor.remove("EFFECT");
                    editor.commit();
                    editor.putInt("EFFECT", 1);
                    editor.commit();
                    effectMode = 1;
                } else {
                    // The toggle is disabled
                    editor.remove("EFFECT");
                    editor.commit();
                    editor.putInt("EFFECT", 0);
                    editor.commit();
                    effectMode = 1;
                }
            }
        });
    }

//    void save() {        Values.HP = Integer.parseInt(textSizeHP.getText().toString());    }

    public void backOnClick(View view) {
        if (effectMode != 0) {
            MediaPlayer mPlayer = MediaPlayer.create(this, buttonEffect);
            mPlayer.start();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}