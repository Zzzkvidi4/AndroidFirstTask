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
    private int difficulty;

    public GameFieldController(Activity activity, int fieldWidth, int fieldHeight, int difficulty){
        gameField = new GameField(fieldWidth, fieldHeight);
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.activity = activity;
        this.difficulty = difficulty;
    }

    @Override
    public void initializeField(int canvasWidth, int canvasHeight){
        gameField.initialize(canvasWidth, canvasHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        int radius = gameField.getElemWidth(canvas.getWidth())/2;
        for (int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                SeqRepeaterGameObject elem = gameField.getField()[i][j];
                mPaint.setColor(elem.getColor());
                canvas.drawCircle(elem.getX()+radius, elem.getY()+radius, radius, mPaint);
                canvas.save();
            }
        }
    }

    @Override
    public void processTouch(MotionEvent event) {
        if (isTouchable) {
            gameField.checkOnTouch((int) event.getX(), (int) event.getY());
        }
        if (gameField.gameFinished()) {
            Timer timer = new Timer();
            timer.schedule(new ShowFinalScoreTimerTask(), 500);
        }
    }

    @Override
    public void startGameCycle() {
        int delayShow = 500;
        int delay = fieldHeight*fieldWidth*GameField.ONE_CARD_DELAY + delayShow;
        Timer timer = new Timer();
        timer.schedule(new ShowWinTimerTask(), delayShow);
        timer.schedule(new StartGameTimerTask(), delay);
    }

    private class ShowWinTimerTask extends TimerTask {
        @Override
        public void run() {
            gameField.showWin();
        }
    }

    private class StartGameTimerTask extends TimerTask {
        @Override
        public void run() {
            isTouchable = true;
        }
    }

    private class ShowFinalScoreTimerTask extends TimerTask {
        @Override
        public void run() {
            Intent intent = new Intent(activity, GameFinishedActivity.class);
            intent.putExtra("score", gameField.getGuessedCount());
            intent.putExtra("id", 2);
            intent.putExtra("difficulty", difficulty);
            activity.finish();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }
}
