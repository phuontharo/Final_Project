package com.example.final_project.entity;

import android.widget.ImageButton;

public class Node {
    private int x;
    private int y;
    private ImageButton button;
    private int value;
    private boolean isVisited;

    public Node(int x, int y, ImageButton button, int values, boolean isVisited) {

        this.x = x;
        this.y = y;
        this.button = button;
        this.value = values;
        this.isVisited = isVisited;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ImageButton getButton() {
        return button;
    }

    public void setButton(ImageButton button) {
        this.button = button;
    }

    public int getValue() {
        return value;
    }

    public void setValues(int values) {
        this.value = values;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

}
