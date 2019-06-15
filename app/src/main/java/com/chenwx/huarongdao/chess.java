package com.chenwx.huarongdao;

import android.graphics.Paint;

public class chess {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean isGoal;

    chess(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(this.width == 400-10 && this.height == 400-10){
            this.isGoal = true;
        }
        else{
            this.isGoal = false;
        }
    }

    chess(chess a){
        this.x  = a.x;
        this.y = a.y;
        this.width = a.width;
        this.height = a.height;
    }

    public boolean isIn(int inputX, int inputY){
        return inputX>=x && inputX<x+width && inputY>=y && inputY<y+height;
    }

    public void adjustChess(int x, int y){
        this.x = x;
        this.y = y;
    }
}
