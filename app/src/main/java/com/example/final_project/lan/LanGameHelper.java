package com.example.final_project.lan;

import android.widget.ImageButton;

import com.example.final_project.controller.Controller;
import com.example.final_project.entity.Message;
import com.example.final_project.entity.Node;
import com.example.final_project.entity.Values;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class LanGameHelper {

    public static void sendPoint(int x, int y, String mess, ObjectOutputStream oOut){
        Message message = new Message(mess, x, y);
        try {
            oOut.writeObject(message);
            System.out.println("Write Object ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
