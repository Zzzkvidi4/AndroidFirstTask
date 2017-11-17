package zzzkvidi4.com.testandroidapplication1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import zzzkvidi4.com.testandroidapplication1.specks.CardField;
import zzzkvidi4.com.testandroidapplication1.specks.CardGameObject;
import zzzkvidi4.com.testandroidapplication1.tools.BitmapTools;

public class SpecksGameActivity extends AppCompatActivity {
    private int difficulty;
    private int score;
    private boolean isFirst;
    private int id;

    private TextView infoTextView;
    LinearLayout gameWidgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        id = intent.getIntExtra("id", -1);
        //setContentView(R.layout.activity_specks_game);
        //infoTextView = (TextView)findViewById(R.id.infoTextView);
        //infoTextView.append("Diff: " + difficulty + "; Score: " + score);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameWidgets = new LinearLayout(this);

        Button pauseBtn = new Button(this);
        pauseBtn.setText("Pause");
        pauseBtn.setOnClickListener(new PauseOnClickListener());

        gameWidgets.addView(pauseBtn);

        FrameLayout game = new FrameLayout(this);
        SurfaceView gameView = new GameView(this, 2, 3);

        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);
    }

    @Override
    public void onBackPressed() {
    }

    public class GameView extends SurfaceView{
        private CardField cardField;
        private GameThread gameThread;
        private boolean isTouchable;

        public GameView(Context context, int fieldWidth, int fieldHeight) {
            super(context);
            cardField = new CardField(fieldWidth, fieldHeight);
            this.gameThread = new GameThread(this);
            getHolder().addCallback(new SurfaceHolder.Callback()
            {
                /*** Уничтожение области рисования */
                public void surfaceDestroyed(SurfaceHolder holder)
                {
                    boolean retry = true;
                    gameThread.setRunning(false);
                    while (retry)
                    {
                        try
                        {
                            // ожидание завершение потока
                            gameThread.join();
                            retry = false;
                        }
                        catch (InterruptedException e) { }
                    }
                }

                /** Создание области рисования */
                public void surfaceCreated(SurfaceHolder holder)
                {
                    gameThread.setRunning(true);
                    gameThread.start();
                }

                /** Изменение области рисования */
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
                {
                }
            });
        }

        private class GameThread extends Thread {
            private boolean isRunning;
            private GameView view;

            GameThread(GameView view){
                this.view = view;
            }

            void setRunning(boolean isRunning){
                this.isRunning = isRunning;
            }

            @Override
            public void run() {
                isTouchable = false;
                Canvas canvas = view.getHolder().lockCanvas();
                draw(canvas);
                if (canvas != null){
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
                cardField.initialize(getWidth(), getHeight(), 50, view);
                cardField.setFieldHidden(false);
                try
                {
                    // подготовка Canvas-а
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder())
                    {
                        draw(canvas);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally
                {
                    if (canvas != null)
                    {
                        view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                cardField.setFieldHidden(true);
                isTouchable = true;
                while (isRunning && !cardField.gameFinished())
                {
                    canvas = null;
                    try
                    {
                        // подготовка Canvas-а
                        canvas = view.getHolder().lockCanvas();
                        synchronized (view.getHolder())
                        {
                            draw(canvas);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (canvas != null)
                        {
                            view.getHolder().unlockCanvasAndPost(canvas);
                        }
                    }
                }
                if (cardField.gameFinished()) {
                    Intent intent = new Intent(SpecksGameActivity.this, GameFinishedActivity.class);
                    intent.putExtra("difficulty", difficulty);
                    intent.putExtra("mistakes", 0);
                    intent.putExtra("score", 0);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                finish();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawColor(Color.WHITE);
            cardField.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if ((isTouchable) && (event.getAction() == MotionEvent.ACTION_DOWN)) {
                cardField.checkOnTouch((int) event.getX(), (int) event.getY());
            }
            return true;
        }
    }

    private class PauseOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SpecksGameActivity.this, GamePauseActivity.class);
            startActivity(intent);
        }
    }
}
