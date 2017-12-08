package zzzkvidi4.com.testandroidapplication1.seqRepeater;

import zzzkvidi4.com.testandroidapplication1.SpecksGameActivity;

/**
 * Created by Red Sky on 08.12.2017.
 */

public class SeqRepeaterGameObject {
    private int x, y;
    private int id;

    SeqRepeaterGameObject(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int hetId()
    {
        return id;
    }
}
