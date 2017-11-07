package zzzkvidi4.com.testandroidapplication1.specks;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import zzzkvidi4.com.testandroidapplication1.SpecksGameActivity;

/**
 * Created by Roman on 07.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.specks.
 */

public class CardGameObject {
    private int x, y;
    private Bitmap faceImage;
    private Bitmap hiddenImage;
    private boolean isHidden;

    public CardGameObject(int x, int y, Bitmap faceImage, Bitmap hiddenImage) {
        this.x = x;
        this.y = y;
        this.faceImage = faceImage;
        this.hiddenImage = hiddenImage;
    }

    public void setHiiden(boolean hidden){
        isHidden = hidden;
    }

    public void onDraw(Canvas canvas){
        if (isHidden) {
            canvas.drawBitmap(hiddenImage, x, y, null);
        } else {
            canvas.drawBitmap(faceImage, x, y, null);
        }
    }
}
