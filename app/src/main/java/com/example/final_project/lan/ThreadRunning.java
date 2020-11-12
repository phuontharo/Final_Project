package com.example.final_project.lan;

import com.example.final_project.entity.Message;

public interface ThreadRunning {
    public void pressNodeByMessage(Message mess);
    public void sendPoint(int x, int y, String mess);
    public boolean isTurn();
    public void setTurn(boolean turn);
    public void setMessage(Message message);
}
