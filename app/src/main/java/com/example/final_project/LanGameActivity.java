package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.final_project.entity.Player;
import com.example.final_project.entity.Values;

public class LanGameActivity extends AppCompatActivity {

    Player player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_game);

        Bundle bundle = getIntent().getExtras();
        player = (Player) bundle.get("platyer");
    }

    public void onClickCreateHost(View view) {
       openPlayMode(Values.HOST);
    }

    public void onClickCreateGuest(View view) {
        openPlayMode(Values.GUEST);
    }

    private void openPlayMode(int mode){
        Intent intent = new Intent(this, ControllerLanGameActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("player", player);
        startActivity(intent);
    }
}