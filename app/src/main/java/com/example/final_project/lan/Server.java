package com.example.final_project.lan;

import android.widget.ImageButton;

import com.example.final_project.ControllerLanGameActivity;
import com.example.final_project.controller.ControllerGame;
import com.example.final_project.entity.Message;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread implements ThreadRunning{

    private ServerSocket server;
    private Socket socket;
    private ObjectOutputStream oOut;
    private ObjectInputStream oIn;
    private Node[][] table;
    private ControllerGame controller;
    boolean dataIn;
    boolean dataOut;
    boolean is_turn = true;

    Message messageOut = null;

    public Server(Node[][] table, ControllerGame controller) {
        this.table = table;
        this.controller = controller;
        dataIn = false;
        dataOut = true;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(9999);

            socket = server.accept();
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());

            while(true){
                if(dataOut){
                    if(messageOut != null){
                        sendPoint(messageOut.getX(), messageOut.getY(), messageOut.getMessage());
                        messageOut = null;
                        dataOut = false;
                        dataIn = true;
                    }
                }
                if(dataIn){
                    Message message = (Message) oIn.readObject();
                    pressNodeByMessage(message);
                    dataIn = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(Message message) {
        this.messageOut = message;
        dataOut = true;
    }

    @Override
    public void pressNodeByMessage(Message mess){
        final int x = mess.getX();
        final int y = mess.getY();
        final ImageButton image = table[x][y].getButton();
        image.post(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(Values.white_chess);
                controller.exec(x,y, Values.valueWhite);
                is_turn = true;
            }
        });
    }

    @Override
    public void sendPoint(int x, int y, String mess){
        LanGameHelper.sendPoint(x,y,mess,oOut);
    }

    @Override
    public boolean isTurn() {
        return is_turn;
    }

    @Override
    public void setTurn(boolean turn) {
        this.is_turn = turn;
    }
}
