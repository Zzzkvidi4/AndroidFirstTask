package zzzkvidi4.com.testandroidapplication1.seqRepeater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

import zzzkvidi4.com.testandroidapplication1.GameFinishedActivity;
import zzzkvidi4.com.testandroidapplication1.gameEngine.GameController;

/**
 * Created by Red Sky on 08.12.2017.
 */

public class GameFieldController  implements GameController {
    private GameField gameField;
    private int fieldWidth;
    private int fieldHeight;
    private boolean isTouchable = false;
    private Activity activity;

    public GameFieldController(Activity activity, int fieldWidth, int fieldHeight){
        gameField = new GameField(fieldWidth, fieldHeight);
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.activity = activity;
    }

    @Override
    public void initializeField(int canvasWidth, int canvasHeight){
        gameField.initialize(canvasWidth, canvasHeight);
        int cardWidth = gameField.getElemWidth(canvasWidth);
        int cardHeight = gameField.getElemHeight(canvasHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                SeqRepeaterGameObject elem = gameField.getField()[i][j];
                Paint paint = new Paint();
                paint.setColor(elem.getColor());
                canvas.drawCircle(elem.getX(), elem.getY(), 10, paint);
            }
        }
    }

    @Override
    public void processTouch(MotionEvent event) {
        if (isTouchable) {
            gameField.checkOnTouch((int) event.getX(), (int) event.getY());
        }
        if (gameField.gameFinished()){
            Timer timer = new Timer();
            timer.schedule(new ShowFinalScoreTimerTask(), 2000);
        }
    }

    @Override
    public void startGameCycle() {
        gameField.showWin();
        Timer timer = new Timer();
        timer.schedule(new HideFieldTimerTask(), 2000);
    }

    private class HideFieldTimerTask extends TimerTask {
        @Override
        public void run() {
            isTouchable = true;
        }
    }

    private class ShowFinalScoreTimerTask extends TimerTask {
        @Override
        public void run() {
            Intent intent = new Intent(activity, GameFinishedActivity.class);
            activity.finish();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }
}
