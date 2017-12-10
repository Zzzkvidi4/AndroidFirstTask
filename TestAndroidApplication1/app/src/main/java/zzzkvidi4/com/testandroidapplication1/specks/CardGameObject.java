package zzzkvidi4.com.testandroidapplication1.specks;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import zzzkvidi4.com.testandroidapplication1.SpecksGameActivity;

/**
 * Created by Roman on 07.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.specks.
 */

public class CardGameObject {
    static final int GUESSED = 1;
    static final int NOT_GUESSED = 2;
    private int x, y;
    private int faceImageId;
    private int hiddenImageId;
    private boolean isHidden = true;
    private int type;
    private int state = NOT_GUESSED;

    CardGameObject(int x, int y, int faceImageId, int hiddenImageId, int type) {
        this.x = x;
        this.y = y;
        this.faceImageId = faceImageId;
        this.hiddenImageId = hiddenImageId;
        this.type = type;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    void setHidden(boolean hidden){
        isHidden = hidden;
    }

    void changeVisible(){
        isHidden = !isHidden;
    }

    void setGuessed(){
        state = GUESSED;
    }

    int getState(){
        return state;
    }

    int getType(){
        return type;
    }

    int getFaceImageId(){
        return faceImageId;
    }
}
