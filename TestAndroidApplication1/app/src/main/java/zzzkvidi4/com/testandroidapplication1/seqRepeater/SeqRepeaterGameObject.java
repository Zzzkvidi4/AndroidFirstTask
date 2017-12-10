package zzzkvidi4.com.testandroidapplication1.seqRepeater;

import android.graphics.Color;

import zzzkvidi4.com.testandroidapplication1.SpecksGameActivity;

/**
 * Created by Red Sky on 08.12.2017.
 */

public class SeqRepeaterGameObject {
    private int x, y;
    private int id;
    private int color;

    SeqRepeaterGameObject(int id, int x, int y, int color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId()
    {
        return id;
    }

    public int getColor() { return color;}
    public void setColor(int value) { color = value; }
}
