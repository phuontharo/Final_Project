package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.final_project.entity.Node;

public class LocalGame extends AppCompatActivity {

    Node[][]board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);
    }
}