package com.example.final_project.lan;

import android.widget.ImageButton;

import com.example.final_project.ControllerLanGameActivity;
import com.example.final_project.controller.ControllerGame;
import com.example.final_project.entity.Message;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread implements ThreadRunning{

    private Socket socket;
    private ObjectOutputStream oOut;
    private ObjectInputStream oIn;
    private Node[][] table;
    private ControllerGame controller;
    boolean dataIn;
    boolean dataOut;
    boolean is_turn = false;

    Message messageOut;

    public Client(Node[][] table, ControllerGame controller) {
        this.table = table;
        this.controller = controller;
        dataIn = true;
        dataOut = false;
    }


    @Override
    public void run() {
        try {
            socket = new Socket("192.168.1.15", 9999);
            oOut = new ObjectOutputStream(socket.getOutputStream());
            oIn = new ObjectInputStream(socket.getInputStream());
            while (true) {
                if (dataIn) {
                    Message message = (Message) oIn.readObject();
                    pressNodeByMessage(message);
                    dataIn = false;
                }
                if (dataOut) {
                    sendPoint(messageOut.getX(), messageOut.getY(), messageOut.getMessage());
                    messageOut = null;
                    dataOut = false;
                    dataIn = true;
                }
            }
        } catch (Exception e) {

        }
    }

    public void setMessage(Message message) {
        this.messageOut = message;
        dataOut = true;
    }

    public void pressNodeByMessage(Message mess) {
        final int x = mess.getX();
        final int y = mess.getY();
        final ImageButton image = table[x][y].getButton();
        image.post(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(Values.black_chess);
                controller.exec(x, y, Values.valueBlack);
                is_turn = true;
            }
        });
    }

    public void sendPoint(int x, int y, String mess) {
        System.out.println("Send Point ");
        LanGameHelper.sendPoint(x, y, mess, oOut);
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
