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
    private Bitmap faceImage;
    private Bitmap hiddenImage;
    private boolean isHidden = true;
    private int type;
    private int state = NOT_GUESSED;

    CardGameObject(int x, int y, Bitmap faceImage, Bitmap hiddenImage, int type) {
        this.x = x;
        this.y = y;
        this.faceImage = faceImage;
        this.hiddenImage = hiddenImage;
        this.type = type;
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

    void onDraw(Canvas canvas, int topMargin){
        if (isHidden) {
            canvas.drawBitmap(hiddenImage, x, topMargin + y, null);
        } else {
            canvas.drawBitmap(faceImage, x, topMargin + y, null);
        }
    }
}
