package zzzkvidi4.com.testandroidapplication1.specks;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import zzzkvidi4.com.testandroidapplication1.gameEngine.GameController;
import zzzkvidi4.com.testandroidapplication1.GameFinishedActivity;
import zzzkvidi4.com.testandroidapplication1.R;

/**
 * Created by Roman on 21.11.2017.
 * In package zzzkvidi4.com.testandroidapplication1.specks.
 */

public class CardFieldController implements GameController {
    private static final int[] BITMAP_IDS = new int[] {R.drawable.book, R.drawable.dipper, R.drawable.stan, R.drawable.mable};
    private static final int MAX_SCORE = 2000;
    private static final int[] CARD_FIELD_WIDTH = new int[] {2, 3, 3, 4};
    private static final int[] CARD_FIELD_HEIGHT = new int[] {3, 3, 4, 4};
    private CardField cardField;
    private int fieldWidth;
    private int fieldHeight;
    private Bitmap[] Bitmaps;
    private boolean isTouchable = false;
    private Activity activity;
    private Long startDate;
    private int difficulty;

    public CardFieldController(Activity activity, int difficulty, Resources resources){
        cardField = new CardField(fieldWidth, fieldHeight);
        this.difficulty = difficulty;
        this.fieldHeight = CARD_FIELD_HEIGHT[difficulty];
        this.fieldWidth = CARD_FIELD_WIDTH[difficulty];
        Bitmaps = new Bitmap[fieldWidth * fieldHeight / 2 + 1];
        prepareBitmaps(resources);
        this.activity = activity;
    }

    private void prepareBitmaps(Resources resources){
        for(int i = 0; i < Bitmaps.length; ++i){
            Bitmaps[i] = BitmapFactory.decodeResource(resources, BITMAP_IDS[i]);
        }
    }

    @Override
    public void initializeField(int canvasWidth, int canvasHeight){
        cardField.initialize(canvasWidth, canvasHeight);
        int cardWidth = cardField.getCardWidth(canvasWidth);
        int cardHeight = cardField.getCardHeight(canvasHeight);
        for(int i = 0; i < Bitmaps.length; ++i){
            Bitmaps[i] = Bitmap.createScaledBitmap(Bitmaps[i], cardWidth, cardHeight, false);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < fieldHeight; ++i){
            for (int j = 0; j < fieldWidth; ++j){
                CardGameObject card = cardField.getCardField()[i][j];
                if (card.isHidden()) {
                    canvas.drawBitmap(Bitmaps[0], card.getX(), card.getY(), null);
                } else {
                    canvas.drawBitmap(Bitmaps[card.getFaceImageId()], card.getX(), card.getY(), null);
                }
            }
        }
    }

    @Override
    public void processTouch(MotionEvent event) {
        if (isTouchable) {
            cardField.checkOnTouch((int) event.getX(), (int) event.getY());
        }
        if (cardField.gameFinished()){
            isTouchable = false;
            Long finishDate = Calendar.getInstance().getTime().getTime();
            int score = MAX_SCORE / (int)(finishDate - startDate) - cardField.getMistakes();
            Timer timer = new Timer();
            timer.schedule(new ShowFinalScoreTimerTask(score, difficulty), 2000);
        }
    }

    @Override
    public void startGameCycle() {
        startDate = Calendar.getInstance().getTime().getTime();
        cardField.setFieldHidden(false);
        Timer timer = new Timer();
        timer.schedule(new HideFieldTimerTask(), 2000);
    }

    private class HideFieldTimerTask extends TimerTask {
        @Override
        public void run() {
            cardField.setFieldHidden(true);
            isTouchable = true;
        }
    }

    private class ShowFinalScoreTimerTask extends TimerTask {
        private int score;
        private int difficulty;

        ShowFinalScoreTimerTask(int score, int difficulty){
            this.score = score;
            this.difficulty = difficulty;
        }

        @Override
        public void run() {
            Intent intent = new Intent(activity, GameFinishedActivity.class);
            intent.putExtra("id", 0);
            intent.putExtra("name", "Парные карты");
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("score", score);
            activity.finish();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }
}
