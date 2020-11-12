package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.final_project.controller.ControllerGame;
import com.example.final_project.entity.Message;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Player;
import com.example.final_project.entity.Values;
import com.example.final_project.lan.Client;
import com.example.final_project.lan.Server;
import com.example.final_project.lan.ThreadRunning;

public class ControllerLanGameActivity extends AppCompatActivity {

    private Node[][] board;
    TableLayout table_layout;
    int current_chess;
    ControllerGame controller;
    int MODE;
    Server server;
    Client client;
    int currentValue = 0;
    Player player1, player2;

    TextView name_1, name_2;
    ProgressBar HP1, HP2;
    ImageView avatar1, avatar2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_lan_game);
        init();
    }

    void init() {
        name_1 = findViewById(R.id.tv_lan_name1);
        name_2 = findViewById(R.id.tv_lan_name2);
//--------------
        board = new Node[Values.board_size][Values.board_size];
        table_layout = findViewById(R.id.board);
        controller = new ControllerGame(board);
        settingBoardGame();
        controller.createLittleMatrix();
        Bundle bundle = getIntent().getExtras();
        player1 = (Player) bundle.get("player");


        MODE = (int) bundle.get("mode");
        if(MODE == Values.HOST){
            currentValue = Values.valueBlack;
            current_chess = Values.black_chess;
            server = new Server(board,controller);
            server.start();
        }else if(MODE == Values.GUEST){
            currentValue = Values.valueWhite;
            current_chess = Values.white_chess;
            client = new Client(board, controller);
            client.start();
        }else if(MODE == Values.mode_local){
            current_chess = Values.black_chess;
        }

    }

    void initLan(){
        // Viết code config lan game
    }
    private void changeChessAfterRound() {
        current_chess = current_chess == Values.black_chess ? Values.white_chess : Values.black_chess;
    }

    private void settingBoardGame() {
        int scale_button = Values.board_height / Values.board_size - 10;
        System.out.println("-----" + scale_button);
        for (int i = 0; i < board.length; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < board[0].length; j++) {
                final int x = i;
                final int y = j;
                final ImageButton imageButton = new ImageButton(this);
                imageButton.setImageResource(Values.chess_background_img);
                TableRow.LayoutParams layout = new TableRow.LayoutParams(scale_button, scale_button);
                imageButton.setLayoutParams(layout);
                final Node node = new Node(x, y, imageButton, Values.valueEmpty, false);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MODE == Values.HOST || MODE == Values.GUEST){
                            handleClickChessLan(node, x, y);
                        }
                    }
                });
                board[i][j] = node;
                row.addView(imageButton);
            }
            table_layout.addView(row);
        }
    }

    void handleClickChessLocal(Node node, int x, int y){
        node.getButton().setImageResource(current_chess);
      //  int HP = controller.execute(x,y);
        changeChessAfterRound();
        // -----------
    }

    void handleClickChessLan(Node node, int x, int y){
            if(MODE == Values.GUEST){
                execClickEvent(client,node,x,y);
            }else{
                execClickEvent(server,node,x,y);
            }

    }

    void execClickEvent(ThreadRunning thr,Node node, int x, int y){
        if(thr.isTurn()){
            thr.setTurn(false);
            node.getButton().setImageResource(current_chess);
            int HP = controller.exec(x,y, currentValue);
            thr.setMessage(new Message("ok",x,y));

        }
    }
}