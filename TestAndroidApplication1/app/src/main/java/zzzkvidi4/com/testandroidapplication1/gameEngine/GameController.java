package zzzkvidi4.com.testandroidapplication1.gameEngine;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Roman on 21.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.
 */

public interface GameController {
    void draw(Canvas canvas);
    void processTouch(MotionEvent event);
    void initializeField(int canvasWidth, int canvasHeight);
    void startGameCycle();
}
