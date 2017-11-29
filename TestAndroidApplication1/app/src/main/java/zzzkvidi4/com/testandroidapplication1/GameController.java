package zzzkvidi4.com.testandroidapplication1;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by Roman on 21.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.
 */

public interface GameController {
    public void draw(Canvas canvas);
    public void processTouch(MotionEvent event);
    public void initializeField(int canvasWidth, int canvasHeight);
    public void startGameCycle();
}
