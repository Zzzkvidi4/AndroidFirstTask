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

    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        difficulty = intent.getIntExtra("difficulty", -1);
        score = intent.getIntExtra("score", -1);
        isFirst = intent.getBooleanExtra("isFirst", true);
        //setContentView(R.layout.activity_specks_game);
        //infoTextView = (TextView)findViewById(R.id.infoTextView);
        //infoTextView.append("Diff: " + difficulty + "; Score: " + score);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout gameWidgets = new LinearLayout(this);

        Button pauseBtn = new Button(this);
        pauseBtn.setText("Pause");
        pauseBtn.setOnClickListener(new PauseOnClickListener());

        gameWidgets.addView(pauseBtn);

        FrameLayout game = new FrameLayout(this);
        SurfaceView gameView = new GameView(this, 2, 3, gameWidgets.getHeight());

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
        private int topMargin;

        public GameView(Context context, int fieldWidth, int fieldHeight, int topMargin) {
            super(context);
            int canvasWidth = getWidth();
            int canvasHeight = getHeight();
            this.topMargin = topMargin;
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

            public GameThread(GameView view){
                this.view = view;
            }

            public void setRunning(boolean isRunning){
                this.isRunning = isRunning;
            }

            @Override
            public void run() {
                cardField.initialize(getWidth(), getHeight(), topMargin, view);
                while (isRunning)
                {
                    Canvas canvas = null;
                    try
                    {
                        // подготовка Canvas-а
                        canvas = view.getHolder().lockCanvas();
                        synchronized (view.getHolder())
                        {
                            //draw(canvas);
                            onDraw(canvas);
                        }
                    }
                    catch (Exception e) { }
                    finally
                    {
                        if (canvas != null)
                        {
                            view.getHolder().unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            cardField.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                cardField.checkOnTouch((int) event.getX(), (int) event.getY());
            }
            return true;
        }
    }

    private class PauseOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
